"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { licenseFormSchema, LicenseFormSchema } from "../schemas/licenseSchema";
import { PersonWithLicense } from "../types/Person";
import { submitLicense } from "../services/licenseService";
import PersonSearchField from "./PersonSearchField";
import LicenseTypeSelect from "./LicenseTypeSelect";
import { useRouter } from "next/navigation";
import { getPersonWithLicenseByIdNumber } from "@/services/personService";

export default function LicenseForm() {
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

  // When a person is found, populate documentoTitular and reset clases
  const onPersonFound = (p: PersonWithLicense) => {
    setPerson(p);
    setValue("documentoTitular", p.numeroDocumento);
    setValue("clases", []);
  };

  // Ensure validation runs when person is set

  // Handle form submit
  const onSubmit = async (data: LicenseFormSchema) => {
    try {
      await submitLicense(data);
      alert("Licencia creada exitosamente");

      // Navigate current tab
      router.push(`/license/print/${person?.numeroDocumento}`);

      // Navigate new tab
      const receiptTab = window.open();
      if (receiptTab) {
        receiptTab.location.href = `/license/payment/receipt/${person?.numeroDocumento}`;
      }
    } catch (err) {
      if (err instanceof Error) {
        alert(err.message || "Error al crear licencia");
      }
    }
  };
  const selectedTypes = watch("clases");
  const hasDisallowedSelection =
    person != null &&
    selectedTypes.some((type) => !person.allowedLicenseTypes.includes(type));

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="max-w-xl mx-auto p-4 border rounded shadow">
        <h2 className="text-2xl text-gray-800 font-bold mb-4">
          Alta Nueva Licencia
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
                disabled
                hidden
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
                  value={person.donanteOrganos ? "Sí" : "No"}
                />
              </div>
            </div>

            <div className="mt-4">
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
                value={watch("clases")}
                onChange={(selected) => setValue("clases", selected)}
                allowedTypes={person.allowedLicenseTypes}
                currentTypes={person.currentLicenseTypes}
                currentTypesWarnings={true}
              />
              {errors.clases && (
                <p className="text-red-500 text-sm">{errors.clases.message}</p>
              )}
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
                disabled={!person || !isValid || hasDisallowedSelection}
                className={`px-4 py-2 rounded text-white ${
                  !person || !isValid || hasDisallowedSelection
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
