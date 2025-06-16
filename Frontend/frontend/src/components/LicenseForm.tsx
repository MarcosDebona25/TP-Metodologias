"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { licenseFormSchema, LicenseFormSchema } from "../schemas/licenseSchema";
import { Person } from "../types/Person";
import { submitLicense } from "../services/licenseService";
import PersonSearchField from "./PersonSearchField";
import LicenseTypeSelect from "./LicenseTypeSelect";
import { useRouter } from "next/navigation";


export default function LicenseForm() {
 const [person, setPerson] = useState<Person | null>(null);
 const router = useRouter();

  const { register, handleSubmit, setValue, watch, formState: { errors }, reset } = useForm<LicenseFormSchema>({
    resolver: zodResolver(licenseFormSchema),
    defaultValues: { personId: "", licenseTypes: [] }
  });

  const onPersonFound = (p: Person) => {
    setPerson(p);
    setValue("personId", p.idNumber);
    setValue("licenseTypes", []);
  };

const onSubmit = async (data: LicenseFormSchema) => {
  try {
    await submitLicense(data);
    alert("Licencia creada exitosamente");
    reset();
    setPerson(null); // clear person preview if you're using one
    router.push("/license/new"); 
  } catch (err: any) {
    alert(err.message || "Error al crear licencia");
  }
};

  return (
   <form onSubmit={handleSubmit(onSubmit)}>
  <div className="max-w-xl mx-auto p-4 border rounded shadow">
    <h2 className="text-2xl text-gray-800 font-bold mb-4">Alta Nueva Licencia</h2>

    <PersonSearchField onPersonFound={onPersonFound} />

    {errors.personId && <p className="text-red-500 text-sm">{errors.personId.message}</p>}

    {person && (
      <>
        <div className="space-y-2 mt-4">
          <input
            type="text"
            {...register("personId")}
            disabled
            hidden
          />
          <div>
            <label className="block font-medium text-gray-700">Nombre</label>
            <input
              className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300"
              disabled
              value={person.firstName}
            />
          </div>
          <div>
            <label className="block font-medium text-gray-700">Apellido</label>
            <input className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300" disabled value={person.lastName} />
          </div>
          <div>
            <label className="block font-medium text-gray-700">Fecha de Nacimiento</label>
            <input className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300" disabled value={person.dateOfBirth} />
          </div>
          <div>
            <label className="block font-medium text-gray-700">Dirección</label>
            <input className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300" disabled value={person.address} />
          </div>
          <div>
            <label className="block font-medium text-gray-700">Grupo Sanguíneo y Factor RH</label>
            <input className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300" disabled value={person.bloodType} />
          </div>
          <div>
            <label className="block font-medium text-gray-700">Donante de Órganos</label>
            <input className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300" disabled value={person.donor ? "Sí" : "No"} />
          </div>
        </div>

        <div className="mt-4">
          <LicenseTypeSelect
            value={watch("licenseTypes")}
            onChange={(selected) => setValue("licenseTypes", selected)}
            allowedTypes={person.allowedLicenseTypes}
          />
          {errors.licenseTypes && (
            <p className="text-red-500 text-sm">{errors.licenseTypes.message}</p>
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
            disabled={!person}
            className="px-4 py-2 bg-green-600 text-white rounded"
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

