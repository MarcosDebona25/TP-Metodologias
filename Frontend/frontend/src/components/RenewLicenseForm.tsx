"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { licenseFormSchema, LicenseFormSchema } from "../schemas/licenseSchema";
import { PersonWithLicense } from "../types/Person";
import { submitLicense } from "../services/licenseService";
import { getPersonWithLicenseByIdNumber } from "@/services/personService";
import PersonSearchField from "./PersonSearchField";
import LicenseTypeSelect from "./LicenseTypeSelect";
import { useRouter } from "next/navigation";

export default function RenewLicenseForm() {
  const [person, setPerson] = useState<PersonWithLicense | null>(null);
  const router = useRouter();

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors, isValid },
    reset,
  } = useForm<LicenseFormSchema>({
    resolver: zodResolver(licenseFormSchema),
    defaultValues: {
      documentoTitular: "",
      clases: [],
      observaciones: "",
    },
    mode: "onChange",
    shouldUnregister: false,
  });

  const onPersonFound = (p: PersonWithLicense) => {
    setPerson(p);
    setValue("documentoTitular", p.numeroDocumento);
    setValue("observaciones", p.observaciones || "");
    setValue("clases", []); // license types to renew will be chosen by user
  };

  const onSubmit = async (data: LicenseFormSchema) => {
    try {
      await submitLicense(data);
      alert("Renovación de licencia exitosa");
      reset();
      setPerson(null);
      router.push("/license/renew");
    } catch (err: any) {
      alert(err.message || "Error al renovar licencia");
    }
  };

  const selectedTypes = watch("clases");

  const hasInvalidRenewTypes =
    person &&
    selectedTypes.some((type) => !person.currentLicenseTypes.includes(type));

  //const currentOnlyText =
  //person && `Actualmente posee: ${person.currentLicenseTypes.join(", ")}\n`;

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="max-w-xl mx-auto p-4 border rounded shadow">
        <h2 className="text-2xl text-gray-800 font-bold mb-4">
          Renovar Licencia
        </h2>

        <PersonSearchField
          onPersonFound={onPersonFound}
          getPersonByIdNumber={getPersonWithLicenseByIdNumber}
        />

        <input type="text" {...register("documentoTitular")} disabled hidden />
        {errors.documentoTitular && (
          <p className="text-red-500 text-sm">
            {errors.documentoTitular.message}
          </p>
        )}

        {person && (
          <>
            <div className="space-y-2 mt-4">
              <input
                type="text"
                {...register("documentoTitular")}
                hidden
                disabled
              />

              <div>
                <label className="block font-medium text-gray-700">
                  Nombre
                </label>
                <input
                  className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300"
                  disabled
                  value={person.nombre}
                />
              </div>
              <div>
                <label className="block font-medium text-gray-700">
                  Apellido
                </label>
                <input
                  className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300"
                  disabled
                  value={person.apellido}
                />
              </div>
              <div>
                <label className="block font-medium text-gray-700">
                  Fecha de Nacimiento
                </label>
                <input
                  className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300"
                  disabled
                  value={person.fechaNacimiento}
                />
              </div>
              <div>
                <label className="block font-medium text-gray-700">
                  Dirección
                </label>
                <input
                  className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300"
                  disabled
                  value={person.domicilio}
                />
              </div>
              <div>
                <label className="block font-medium text-gray-700">
                  Grupo Sanguíneo y Factor RH
                </label>
                <input
                  className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300"
                  disabled
                  value={person.grupoFactor}
                />
              </div>

              <div>
                <label className="block font-medium text-gray-700">
                  Donante de Órganos
                </label>
                <input
                  className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300"
                  disabled
                  value={person.donanteOrganos}
                />
              </div>

              <div className="border-t pt-4 mt-4">
                <div>
                  <label className="block font-medium text-gray-700">
                    Fecha de Otorgamiento
                  </label>
                  <input
                    className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300"
                    disabled
                    value={person.fechaEmisionLicencia}
                  />
                </div>

                <div>
                  <label className="block font-medium text-gray-700">
                    Fecha de Vencimiento
                  </label>
                  <input
                    className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300"
                    disabled
                    value={person.fechaVencimientoLicencia}
                  />
                </div>
              </div>

              <div>
                <label className="block font-medium text-gray-700">
                  Observaciones
                </label>
                <textarea
                  {...register("observaciones")}
                  className="w-full rounded text-gray-600 border-gray-300 border-1"
                  placeholder="..."
                />
              </div>

              <div className="mt-4">
                <LicenseTypeSelect
                  value={selectedTypes}
                  onChange={(selected) => setValue("clases", selected)}
                  allowedTypes={person.allowedLicenseTypes} // Only allow renewals of current types
                  currentTypes={person.currentLicenseTypes}
                  currentTypesWarnings={false}
                />
                {errors.clases && (
                  <p className="text-red-500 text-sm">
                    {errors.clases.message}
                  </p>
                )}
                {hasInvalidRenewTypes && (
                  <p className="text-yellow-600 text-sm mt-1">
                    Algunas clases seleccionadas no son parte de las licencias
                    actuales.
                  </p>
                )}
              </div>
            </div>

            <div className="mt-6 flex justify-end gap-4">
              <button
                type="button"
                onClick={() => {
                  reset();
                  setPerson(null);
                }}
                className="px-4 py-2 bg-red-500 text-white rounded"
              >
                Borrar
              </button>
              <button
                type="submit"
                disabled={!person || !isValid || hasInvalidRenewTypes}
                className={`px-4 py-2 rounded text-white ${
                  !person || !isValid || hasInvalidRenewTypes
                    ? "bg-blue-400 cursor-not-allowed"
                    : "bg-blue-600 hover:bg-blue-700"
                }`}
              >
                Crear
              </button>
            </div>
          </>
        )}
      </div>
    </form>
  );
}
