"use client"

import Image from "next/image"
import { useEffect, useState } from "react"
import { LicenciaActiva } from "@/types/License"
import { Person, PersonWithLicense } from "@/types/Person"
import {useParams} from "next/navigation";
import { getPersonByIdNumber, getPersonWithLicenseByIdNumber } from "@/services/personService"



export default function PrintLicensePage() {
    const params = useParams()
    const id = params?.id as string  // 👈 aseguramos que sea string

    const [license, setLicense] = useState<LicenciaActiva | null>(null)
    const [person, setPerson] = useState<Person | null>(null)

    useEffect(() => {
        if (!id) return

        async function fetchData() {
            const res = await fetch(`http://localhost:8080/api/licencias/${id}`)
            const res2 = await fetch(`http://localhost:8080/api/titulares/id/${id}`)
            const licenciaData = await res.json()
            const personData = await res2.json()
            setLicense(licenciaData)
            setPerson(personData.titular)
        }

        fetchData();
    }, [id])

    if (!license || !person) return <p>Cargando licencia...</p>

    return (
        <div className="flex flex-col justify-center items-center min-h-screen bg-[#4b9ce9] print:bg-white">
            <div className="bg-white rounded-xl shadow-lg w-[750px] h-[400px] p-4 relative text-[14px] text-black font-sans print:shadow-none">
                {/* Encabezado */}
                <div className="flex justify-between">
                    <div>
                        <p className="text-sm font-semibold">Licencia Nacional de Conducir</p>
                        <p className="text-xs">Santa Fe Capital - Santa Fe</p>
                    </div>
                    <div>
                        <Image src="/logo-arg.png" width={40} height={40} alt="Escudo"/>
                    </div>
                </div>

                {/* Contenido */}
                <div className="flex mt-2">
                    <div className="w-1/4">
                        <Image
                            src="/foto-documento.jpg"
                            width={140}
                            height={180}
                            alt="Foto"
                            className="rounded border"
                        />
                    </div>

                    <div className="w-3/4 pl-4 grid grid-cols-2 gap-1">
                        <p><b>N° Licencia:</b> {license.documentoTitular}</p>
                        <p><b>Clases:</b> {license.clases}</p>
                        <p><b>Apellido:</b> {license.apellidoTitular}</p>
                        <p><b>Nombre:</b> {license.nombreTitular}</p>
                        <p><b>Domicilio:</b> {license.domicilioTitular}</p>
                        <p><b>Fecha de Nac.:</b> {formatear(person.fechaNacimiento)}</p>
                        <p><b>Otorgamiento:</b> {formatear(license.fechaEmisionLicencia)}</p>
                        <p><b>Vencimiento:</b> {formatear(license.fechaVencimientoLicencia)}</p>
                        <p><b>Donante de Órganos:</b> {license.donanteOrganos}</p>
                        <p><b>Grupo y Factor:</b> {license.grupoFactor}</p>
                        <p className="col-span-2"><b>Observaciones:</b> {license.observacionesLicencia}</p>
                    </div>
                </div>

                <div className="absolute bottom-4 left-4 text-[12px] font-semibold">
                    SEGURIDAD VIAL
                </div>
                <div className="absolute bottom-4 right-4 text-[12px] text-right">
                    Ministerio de Transporte<br/>
                    República Argentina
                </div>
            </div>

            <div className="mt-6 print:hidden">
                <button
                    onClick={() => window.print()}
                    className="px-6 py-2 bg-green-600 text-white rounded-lg shadow hover:bg-green-700"
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
