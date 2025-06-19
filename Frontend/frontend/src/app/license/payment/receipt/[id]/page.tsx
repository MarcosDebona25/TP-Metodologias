'use client'

import { useEffect, useState } from "react"
import { useParams } from "next/navigation"
import { Comprobante } from "@/types/Receipt"

export default function PaymentReceiptPage() {
    const params = useParams()
    const id = params?.id as string

    const [comprobante, setReceipt] = useState<Comprobante | null>(null)

    useEffect(() => {
        if (!id) return

        async function fetchData() {
            try {
                const res = await fetch(`http://localhost:8080/api/licencias/comprobante/${id}`)
                const data = await res.json()
                setReceipt(data)
            } catch (error) {
                console.error("Error de comprobante:", error)
            }
        }

        fetchData()
    }, [id])

    if (!comprobante) return <p>Cargando comprobante...</p>

    const data = {
        id: id,
        nombre: `${comprobante.nombreTitular} ${comprobante.apellidoTitular}`,
        tramite: `Renovación de Licencia Clase ${comprobante.clases}`,
        costoTramite: `${comprobante.costosEmision}`,
        gastosAdm:`${comprobante.costosAdministrativos}` ,
        total: `${comprobante.costosAdministrativos+comprobante.costosEmision}`,
        fechaEmision: `Fecha de emision`,
    }

    return (
        <div className="flex justify-center items-center min-h-screen bg-[#4b9ce9] print:bg-white">
            <div className="bg-white rounded-xl shadow-lg w-[600px] p-6 text-sm font-sans print:shadow-none border border-gray-300 text-black">
                <h1 className="text-xl font-bold mb-4 text-center">Comprobante de Pago</h1>

                <div className="space-y-2">
                    <p><b>Nombre:</b> {data.nombre}</p>
                    <p><b>DNI:</b> {data.id}</p>
                    <p><b>Tipo de trámite:</b> {data.tramite}</p>

                    <div className="border-t border-gray-200 pt-3 mt-2">
                        <p><b>Detalle de Costos:</b></p>
                        <ul className="list-disc list-inside ml-4">
                            <li>Trámite: ${data.costoTramite}</li>
                            <li>Gastos administrativos: ${data.gastosAdm}</li>
                        </ul>
                        <p className="mt-1"><b>Total:</b> ${data.total}</p>
                    </div>

                    <div className="border-t border-gray-200 pt-3 mt-2 text-sm">
                        <p><b>Fecha de emisión:</b> {formatear(data.fechaEmision)}</p>
                    </div>
                </div>

                <p className="text-xs text-center mt-6 print:mt-10">
                    Conserve este comprobante para presentarlo al momento del pago.
                </p>

                <div className="mt-6 flex justify-center print:hidden">
                    <button
                        onClick={() => window.print()}
                        className="px-6 py-2 bg-green-600 text-white rounded-lg shadow hover:bg-green-700"
                    >
                        Imprimir comprobante
                    </button>
                </div>
            </div>
        </div>
    )
}

function formatear(fecha: string) {
    return new Date(fecha).toLocaleDateString("es-AR", {
        day: "2-digit",
        month: "long",
        year: "numeric"
    })
}
