"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import PersonSearchField from "./PersonSearchField";
import { Person } from "../types/Person";
import { personFormSchema, PersonFormSchema } from "@/schemas/personSchema";
import { getPersonByIdNumber, updatePerson } from "@/services/personService";

const validBloodTypes = [
  "A+",
  "A-",
  "B+",
  "B-",
  "AB+",
  "AB-",
  "O+",
  "O-",
] as const;

export default function UpdatePersonForm() {
  const [personFound, setPersonFound] = useState<Person | null>(null);

  const {
    register,
    handleSubmit,
    reset,
    setValue,
    formState: { errors, isValid },
  } = useForm<PersonFormSchema>({
    resolver: zodResolver(personFormSchema),
    mode: "onChange",
  });

  const onPersonFound = (p: Person) => {
    const bloodType = validBloodTypes.includes(p.grupoFactor as any)
      ? (p.grupoFactor as (typeof validBloodTypes)[number])
      : undefined;
    setPersonFound(p);
    reset({
      numeroDocumento: p.numeroDocumento,
      nombre: p.nombre,
      apellido: p.apellido,
      fechaNacimiento: p.fechaNacimiento,
      domicilio: p.domicilio,
      grupoFactor: bloodType,
      donanteOrganos: p.donanteOrganos,
    });
  };

  const onSubmit = async (data: PersonFormSchema) => {
    try {
      await updatePerson(data);
      alert("Persona actualizada correctamente");
      setPersonFound(null);
      reset();
    } catch (err: any) {
      alert(err.message || "Error al actualizar persona");
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="max-w-xl mx-auto p-4 border rounded shadow">
        <h2 className="text-2xl text-gray-800 font-bold mb-4">
          Actualizar Persona
        </h2>

        <PersonSearchField
          onPersonFound={onPersonFound}
          getPersonByIdNumber={getPersonByIdNumber}
        />

        {personFound && (
          <div className="space-y-4 mt-6">
            <div>
              <label className="block font-medium text-gray-700">Nombre</label>
              <input
                {...register("nombre")}
                className="w-full rounded text-gray-800 border-gray-300 border-1"
              />
              {errors.nombre && (
                <p className="text-red-500 text-sm">{errors.nombre.message}</p>
              )}
            </div>

            <div>
              <label className="block font-medium text-gray-700">
                Apellido
              </label>
              <input
                {...register("apellido")}
                className="w-full rounded text-gray-800 border-gray-300 border-1"
              />
              {errors.apellido && (
                <p className="text-red-500 text-sm">
                  {errors.apellido.message}
                </p>
              )}
            </div>

            <div>
              <label className="block font-medium text-gray-700">
                Fecha de nacimiento
              </label>
              <input
                type="date"
                {...register("fechaNacimiento")}
                className="w-full rounded text-gray-800 border-gray-300 border-1"
              />
              {errors.fechaNacimiento && (
                <p className="text-red-500 text-sm">
                  {errors.fechaNacimiento.message}
                </p>
              )}
            </div>

            <div>
              <label className="block font-medium text-gray-700">
                Dirección
              </label>
              <input
                {...register("domicilio")}
                className="w-full rounded text-gray-800 border-gray-300 border-1"
              />
              {errors.domicilio && (
                <p className="text-red-500 text-sm">
                  {errors.domicilio.message}
                </p>
              )}
            </div>

            <div>
              <label className="block font-medium text-gray-700">
                Grupo Sanguíneo
              </label>
              <select
                {...register("grupoFactor")}
                className="w-full rounded text-gray-800 border-gray-300 border-1"
              >
                <option value="">Seleccione una opción</option>
                <option value="A+">A+</option>
                <option value="A-">A-</option>
                <option value="B+">B+</option>
                <option value="B-">B-</option>
                <option value="AB+">AB+</option>
                <option value="AB-">AB-</option>
                <option value="O+">O+</option>
                <option value="O-">O-</option>
              </select>
              {errors.grupoFactor && (
                <p className="text-red-500 text-sm">
                  {errors.grupoFactor.message}
                </p>
              )}
            </div>

            <div className="flex items-center gap-2">
              <input
                type="checkbox"
                {...register("donanteOrganos")}
                id="donanteOrganos"
              />
              <label
                htmlFor="donanteOrganos"
                className="text-gray-700 font-medium"
              >
                Donante de órganos
              </label>
            </div>

            <div className="mt-6 flex justify-end gap-4">
              <button
                type="button"
                onClick={() => {
                  reset();
                  setPersonFound(null);
                }}
                className="px-4 py-2 bg-red-500 text-white rounded"
              >
                Borrar
              </button>
              <button
                type="submit"
                disabled={!isValid}
                className={`px-4 py-2 rounded text-white ${
                  !isValid
                    ? "bg-blue-400 cursor-not-allowed"
                    : "bg-blue-600 hover:bg-blue-700"
                }`}
              >
                Actualizar
              </button>
            </div>
          </div>
        )}
      </div>
    </form>
  );
}
