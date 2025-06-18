'use client'

import Image from "next/image"

interface Props {
    params: { id: string }
}

export default async function PrintLicensePage({ params }: Props) {
    const id = params.id

    // 🔧 Simulación de fetch; reemplazar por API real
    const license = await getMockLicenseById(id)

    return (
        <div className="flex flex-col justify-center items-center min-h-screen bg-[#4b9ce9] print:bg-white">
            <div
                className="bg-white rounded-xl shadow-lg w-[750px] h-[400px] p-4 relative text-[14px] text-black font-sans print:shadow-none">
                {/* Encabezado */}
                <div className="flex justify-between">
                    <div>
                        <p className="text-sm font-semibold">Licencia Nacional de Conducir</p>
                        <p className="text-xs">Rosario - Santa Fe</p>
                    </div>
                    <div>
                        <Image src="/logo-arg.png" width={40} height={40} alt="Escudo"/>
                    </div>
                </div>

                {/* Contenido */}
                <div className="flex mt-2">
                    {/* Foto */}
                    <div className="w-1/4">
                        <Image
                            src={license.fotoUrl}
                            width={140}
                            height={180}
                            alt="Foto"
                            className="rounded border"
                        />
                    </div>

                    {/* Datos */}
                    <div className="w-3/4 pl-4 grid grid-cols-2 gap-1">
                        <p><b>N° Licencia:</b> {license.licencia}</p>
                        <p><b>Clases:</b> {license.clase}</p>

                        <p><b>Apellido:</b> {license.apellido}</p>
                        <p><b>Nombre:</b> {license.nombre}</p>

                        <p><b>Domicilio:</b> {license.direccion}</p>
                        <p><b>Fecha de Nac.:</b> {formatear(license.fechaNacimiento)}</p>

                        <p><b>Otorgamiento:</b> {formatear(license.fechaEmision)}</p>
                        <p><b>Vencimiento:</b> {formatear(license.fechaVencimiento)}</p>

                        <p><b>Donante de Organos:</b> {license.esDonante}</p>
                        <p><b>Grupo y Factor:</b> {license.grupoFactor}</p>

                        <p className="col-span-2"><b>Observaciones:</b> {license.observaciones}</p>
                    </div>
                </div>

                {/* Pie de página */}
                <div className="absolute bottom-4 left-4 text-[12px] font-semibold">
                    SEGURIDAD VIAL
                </div>
                <div className="absolute bottom-4 right-4 text-[12px] text-right">
                    Ministerio de Transporte<br/>
                    República Argentina
                </div>
            </div>

            {/* Botón de impresión - fuera del área imprimible */}
            <div className="mt-6 print:hidden">
                <button
                    onClick={() => window.print()}
                    className="px-6 py-2 bg-blue-600 text-white rounded-lg shadow hover:bg-blue-700"
                >
                    Imprimir licencia
                </button>
            </div>
            <style jsx global>{`
                @media print {
                    body {
                        margin: 0;
                    }

                    @page {
                        size: auto;
                        margin: 0;
                    }

                    /* 👇 Fuerza a imprimir bordes, sombras y estilos visuales */
                    * {
                        -webkit-print-color-adjust: exact !important;
                        print-color-adjust: exact !important;
                        box-shadow: none !important;
                    }

                    .rounded-xl {
                        border-radius: 12px !important;
                        border: 1px solid #000 !important;
                    }
                }
            `}</style>
        </div>
    )
}

function formatear(fecha: string) {
    return new Date(fecha).toLocaleDateString("es-AR", {
        day: "2-digit",
        month: "short",
        year: "numeric"
    })
}

async function getMockLicenseById(id: string) {
    return {
        nombre: "LIONEL ANDRES",
        apellido: "MESSI",
        licencia: id,
        direccion: "Mitre 559",
        fechaNacimiento: "1987-06-24",
        fechaEmision: "2025-06-16",
        fechaVencimiento: "2028-06-24",
        clase: "A3 B1",
        fotoUrl: "/foto-documento.jpg",
        grupoFactor: "A+",
        esDonante: "SÍ",
        observaciones: "Utiliza lentes, texto muy muy largoooooooooooo",
    }
}
