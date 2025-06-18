// app/person/new/NewPersonForm.tsx
"use client";

import { useState } from "react";
import { submitPerson } from "@/services/personService";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { personFormSchema, PersonFormSchema } from "@/schemas/personSchema";

export default function NewPersonForm() {
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<PersonFormSchema>({
    resolver: zodResolver(personFormSchema),
    defaultValues: {
      numeroDocumento: "",
      nombre: "",
      apellido: "",
      fechaNacimiento: "",
      domicilio: "",
      grupoFactor: undefined,
      donanteOrganos: false,
    },
  });

  const onSubmit = async (data: PersonFormSchema) => {
    try {
      await submitPerson(data);
      setSuccessMessage("Persona creada exitosamente");
      setErrorMessage("");
      reset();
    } catch (error: any) {
      setErrorMessage(error.message);
      setSuccessMessage("");
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="max-w-xl mx-auto p-4 border rounded shadow">
        <h2 className="text-2xl text-gray-800 font-bold mb-4">
          Alta Nueva Persona
        </h2>

        <div className="space-y-4">
          <div>
            <label className="block font-medium text-gray-700">
              Número de DNI
            </label>
            <input
              type="text"
              {...register("numeroDocumento")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
              placeholder="Ej: 12345678"
            />
            {errors.numeroDocumento && (
              <p className="text-red-500 text-sm">
                {errors.numeroDocumento.message}
              </p>
            )}
          </div>

          <div>
            <label className="block font-medium text-gray-700">Nombres</label>
            <input
              type="text"
              {...register("nombre")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
            />
            {errors.nombre && (
              <p className="text-red-500 text-sm">{errors.nombre.message}</p>
            )}
          </div>

          <div>
            <label className="block font-medium text-gray-700">Apellidos</label>
            <input
              type="text"
              {...register("apellido")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
            />
            {errors.apellido && (
              <p className="text-red-500 text-sm">{errors.apellido.message}</p>
            )}
          </div>

          <div>
            <label className="block font-medium text-gray-700">
              Fecha de Nacimiento
            </label>
            <input
              type="date"
              {...register("fechaNacimiento")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
            />
            {errors.fechaNacimiento && (
              <p className="text-red-500 text-sm">
                {errors.fechaNacimiento.message}
              </p>
            )}
          </div>

          <div>
            <label className="block font-medium text-gray-700">Domicilio</label>
            <input
              type="text"
              {...register("domicilio")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
            />
            {errors.domicilio && (
              <p className="text-red-500 text-sm">{errors.domicilio.message}</p>
            )}
          </div>

          <div>
            <label className="block font-medium text-gray-700">
              Grupo Sanguíneo y Factor RH
            </label>
            <select
              {...register("grupoFactor")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
            >
              <option value="">Selecciona un grupo sanguíneo</option>
              <option value="A+">A+</option>
              <option value="A-">A−</option>
              <option value="B+">B+</option>
              <option value="B-">B−</option>
              <option value="AB+">AB+</option>
              <option value="AB-">AB−</option>
              <option value="O+">O+</option>
              <option value="O-">O−</option>
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
              className="h-4 w-4 text-blue-600"
            />
            {errors.donanteOrganos && (
              <p className="text-red-500 text-sm">
                {errors.donanteOrganos.message}
              </p>
            )}
            <label className="text-gray-700 font-medium">
              Donante de Órganos
            </label>
          </div>
        </div>
        {successMessage && <p className="text-green-600">{successMessage}</p>}
        {errorMessage && <p className="text-red-600">{errorMessage}</p>}
        <div className="mt-6 flex justify-end gap-4">
          <button
            type="button"
            onClick={() => reset()}
            className="px-4 py-2 bg-red-500 text-white rounded"
          >
            Cancelar
          </button>
          <button
            type="submit"
            className="px-4 py-2 bg-blue-600 text-white rounded"
          >
            Crear Persona
          </button>
        </div>
      </div>
    </form>
  );
}
