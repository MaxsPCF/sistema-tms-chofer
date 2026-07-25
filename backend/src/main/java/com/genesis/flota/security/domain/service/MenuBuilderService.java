package com.genesis.flota.security.domain.service;

import com.genesis.flota.security.domain.model.Modulo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuBuilderService {

    /**
     * Construye el árbol de menús filtrado por permisos.
     *
     * Lógica IDÉNTICA a .NET:
     * 1. Todos los módulos activos
     * 2. Permisos solo en hojas (módulos con ruta)
     * 3. OR lógico entre roles
     * 4. Construir árbol recursivo
     * 5. Excluir hojas sin permiso y padres vacíos
     */
    public List<Modulo> construirArbolMenus(List<Modulo> modulosConPermisos) {
        if (modulosConPermisos == null || modulosConPermisos.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. Separar módulos con permiso (hojas) de los que no
        // Los módulos con ruta != null son "hojas" y son los que tienen permisos
        Map<Integer, Modulo> permisosPorModulo = modulosConPermisos.stream()
                .filter(m -> m.getRuta() != null) // Solo hojas
                .collect(Collectors.toMap(
                        Modulo::getIdModulo,
                        m -> m,
                        (existing, replacement) -> {
                            // OR lógico: si hay duplicados (múltiples roles), unir permisos
                            return Modulo.builder()
                                    .idModulo(existing.getIdModulo())
                                    .idModuloPadre(existing.getIdModuloPadre())
                                    .nombreModulo(existing.getNombreModulo())
                                    .icono(existing.getIcono())
                                    .ruta(existing.getRuta())
                                    .orden(existing.getOrden())
                                    .puedeVer(existing.isPuedeVer() || replacement.isPuedeVer())
                                    .puedeCrear(existing.isPuedeCrear() || replacement.isPuedeCrear())
                                    .puedeEditar(existing.isPuedeEditar() || replacement.isPuedeEditar())
                                    .puedeEliminar(existing.isPuedeEliminar() || replacement.isPuedeEliminar())
                                    .puedeAprobar(existing.isPuedeAprobar() || replacement.isPuedeAprobar())
                                    .puedeExportar(existing.isPuedeExportar() || replacement.isPuedeExportar())
                                    .build();
                        }));

        // 2. Obtener TODOS los módulos necesarios para el árbol
        // Incluir padres que no tienen permiso directo pero tienen hijos con permiso
        Set<Integer> modulosRequeridos = new HashSet<>(permisosPorModulo.keySet());

        // Agregar padres recursivamente
        for (Modulo m : modulosConPermisos) {
            if (m.getIdModuloPadre() != null) {
                agregarPadres(modulosConPermisos, m.getIdModuloPadre(), modulosRequeridos);
            }
        }

        // 3. Construir mapa de todos los módulos por ID
        Map<Integer, Modulo> todosModulos = modulosConPermisos.stream()
                .collect(Collectors.toMap(Modulo::getIdModulo, m -> m, (a, b) -> a));

        // 4. Construir árbol recursivo
        return buildArbol(todosModulos, permisosPorModulo, modulosRequeridos, null);
    }

    /**
     * Agrega recursivamente los padres al conjunto de módulos requeridos.
     */
    private void agregarPadres(List<Modulo> todos, Integer idModulo, Set<Integer> requeridos) {
        if (idModulo == null || requeridos.contains(idModulo)) return;
        requeridos.add(idModulo);

        todos.stream()
                .filter(m -> m.getIdModulo().equals(idModulo) && m.getIdModuloPadre() != null)
                .findFirst()
                .ifPresent(padre -> agregarPadres(todos, padre.getIdModuloPadre(), requeridos));
    }

    /**
     * Construye el árbol recursivamente.
     * IDÉNTICO a BuildArbol de .NET
     */
    private List<Modulo> buildArbol(
            Map<Integer, Modulo> todos,
            Map<Integer, Modulo> permisosPorModulo,
            Set<Integer> modulosRequeridos,
            Integer idPadre) {

        List<Modulo> resultado = new ArrayList<>();

        // Obtener hijos del nivel actual, ordenados
        List<Modulo> hijos = todos.values().stream()
                .filter(m -> Objects.equals(m.getIdModuloPadre(), idPadre))
                .filter(m -> m.isActivo())
                .filter(m -> modulosRequeridos.contains(m.getIdModulo()))
                .sorted(Comparator.comparing(Modulo::getOrden))
                .toList();

        for (Modulo m : hijos) {
            // Recursivo para los hijos
            List<Modulo> hijosRecursivos = buildArbol(todos, permisosPorModulo,
                    modulosRequeridos, m.getIdModulo());

            boolean esHoja = m.getRuta() != null;
            boolean tienePermiso = permisosPorModulo.containsKey(m.getIdModulo());

            // Excluir hojas sin permiso
            if (esHoja && !tienePermiso) continue;

            // Excluir agrupadores sin hijos visibles
            if (!esHoja && hijosRecursivos.isEmpty()) continue;

            // Construir el módulo con sus permisos
            Modulo.ModuloBuilder builder = Modulo.builder()
                    .idModulo(m.getIdModulo())
                    .idModuloPadre(m.getIdModuloPadre())
                    .nombreModulo(m.getNombreModulo())
                    .descripcion(m.getDescripcion())
                    .icono(m.getIcono())
                    .ruta(m.getRuta())
                    .orden(m.getOrden())
                    .activo(m.isActivo())
                    .children(hijosRecursivos);

            // Asignar permisos (solo si es hoja y tiene permisos)
            if (tienePermiso) {
                Modulo permiso = permisosPorModulo.get(m.getIdModulo());
                builder.puedeVer(permiso.isPuedeVer())
                        .puedeCrear(permiso.isPuedeCrear())
                        .puedeEditar(permiso.isPuedeEditar())
                        .puedeEliminar(permiso.isPuedeEliminar())
                        .puedeAprobar(permiso.isPuedeAprobar())
                        .puedeExportar(permiso.isPuedeExportar());
            } else {
                // Padres sin permiso directo
                builder.puedeVer(false)
                        .puedeCrear(false)
                        .puedeEditar(false)
                        .puedeEliminar(false)
                        .puedeAprobar(false)
                        .puedeExportar(false);
            }

            resultado.add(builder.build());
        }

        return resultado;
    }
}