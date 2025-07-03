"use client"
import { useState } from "react"

const gruposSanguineos = [
    "", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
]

export default function BuscarLicencias() {
    const [filtros, setFiltros] = useState({
        nombre: "",
        apellido: "",
        grupoSanguineo: "",
        donanteOrganos: null as boolean | null
    })
    const [licencias, setLicencias] = useState([])
    const [buscado, setBuscado] = useState(false)

    const buscarLicencias = async () => {
        const params = new URLSearchParams()
        if (filtros.nombre) params.append("nombre", filtros.nombre)
        if (filtros.apellido) params.append("apellido", filtros.apellido)
        if (filtros.grupoSanguineo) params.append("grupoFactor", filtros.grupoSanguineo)
        if (filtros.donanteOrganos !== null) {
            params.append("esDonante", filtros.donanteOrganos.toString())
        }

        try {
            const res = await fetch(`/api/licenses/filters?${params.toString()}`)
            if (!res.ok) throw new Error("Error en la respuesta del servidor")

            const data = await res.json()
            setLicencias(data)
        } catch (err) {
            console.error("Error al buscar licencias:", err)
            setLicencias([])
        } finally {
            setBuscado(true)
        }
    }

    return (
        <div className="min-h-screen flex flex-col items-center py-10 bg-white text-black">
            <h1 className="text-2xl font-bold mb-6">Buscar Licencias Vigentes</h1>

            {/* Filtros */}
            <div className="w-full max-w-6xl flex flex-wrap items-end gap-4 justify-center mb-8">
                <input
                    type="text"
                    placeholder="Nombre"
                    value={filtros.nombre}
                    onChange={(e) => setFiltros({ ...filtros, nombre: e.target.value })}
                    className="p-2 border border-gray-300 rounded w-48"
                />
                <input
                    type="text"
                    placeholder="Apellido"
                    value={filtros.apellido}
                    onChange={(e) => setFiltros({ ...filtros, apellido: e.target.value })}
                    className="p-2 border border-gray-300 rounded w-48"
                />
                <select
                    value={filtros.grupoSanguineo}
                    onChange={(e) => setFiltros({ ...filtros, grupoSanguineo: e.target.value })}
                    className="p-2 border border-gray-300 rounded w-48"
                >
                    {gruposSanguineos.map((grupo) => (
                        <option key={grupo} value={grupo}>{grupo || "Grupo sanguíneo"}</option>
                    ))}
                </select>

                {/* Donante como dropdown */}
                <select
                    value={filtros.donanteOrganos === null ? "" : filtros.donanteOrganos.toString()}
                    onChange={(e) => {
                        const value = e.target.value
                        setFiltros({
                            ...filtros,
                            donanteOrganos: value === "" ? null : value === "true"
                        })
                    }}
                    className="p-2 border border-gray-300 rounded w-48"
                >
                    <option value="">¿Donante?</option>
                    <option value="true">Sí</option>
                    <option value="false">No</option>
                </select>

                <button
                    className="bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700"
                    onClick={buscarLicencias}
                >
                    Buscar
                </button>
            </div>

            {/* Resultados */}
            {licencias.length > 0 ? (
                <div className="w-full max-w-6xl overflow-x-auto">
                    <table className="w-full border border-gray-300 text-center">
                        <thead className="bg-gray-100">
                        <tr>
                            <th className="p-2 border">Nombre</th>
                            <th className="p-2 border">Apellido</th>
                            <th className="p-2 border">DNI</th>
                            <th className="p-2 border">Grupo</th>
                            <th className="p-2 border">Donante</th>
                            <th className="p-2 border">Clases</th>
                            <th className="p-2 border">Fecha de Emisión</th>
                            <th className="p-2 border">Fecha de Venciminto</th>
                            <th className="p-2 border">Acción</th>
                        </tr>
                        </thead>
                        <tbody>
                        {licencias.map((licencia: any) => (
                            <tr key={licencia.id} className="border-t">
                                <td className="p-2 border">{licencia.nombreTitular}</td>
                                <td className="p-2 border">{licencia.apellidoTitular}</td>
                                <td className="p-2 border">{licencia.documentoTitular}</td>
                                <td className="p-2 border">{licencia.grupoFactor}</td>
                                <td className="p-2 border">{licencia.donanteOrganos === true || licencia.donanteOrganos === "true" ? "Sí" : "No"}</td>
                                <td className="p-2 border">{licencia.clases}</td>
                                <td className="p-2 border">{licencia.fechaEmisionLicencia}</td>
                                <td className="p-2 border">{licencia.fechaVencimientoLicencia}</td>
                                <td className="p-2 border">
                                    <a
                                        href={`/license/print/${licencia.documentoTitular}`}
                                        target="_blank"
                                        className="text-blue-600 underline hover:text-blue-800"
                                    >
                                        Imprimir
                                    </a>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                buscado && (
                    <p className="text-gray-600 mt-6">
                        No se encontraron licencias con los filtros seleccionados.
                    </p>
                )
            )}
        </div>
    )
}
