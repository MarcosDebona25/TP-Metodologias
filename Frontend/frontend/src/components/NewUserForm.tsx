"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { userFormSchema, UserFormSchema } from "@/schemas/userSchema";
import { submitUser } from "@/services/userService";

export default function NewUserForm() {
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<UserFormSchema>({
    resolver: zodResolver(userFormSchema),
    defaultValues: {
      numeroDocumento: "",
      nombre: "",
      apellido: "",
      email: "",
      rol: "USER",
      contrasena: "",
      confirmPassword: "",
    },
  });

  const onSubmit = async (data: UserFormSchema) => {
    try {
      await submitUser(data);
      setSuccessMessage("Usuario creado exitosamente");
      setErrorMessage("");
      reset();
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage(error.message);
        setSuccessMessage("");
      }
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="max-w-xl mx-auto p-4 border rounded shadow">
        <h2 className="text-2xl font-bold text-gray-800 mb-4">
          Registrar Usuario
        </h2>

        <div className="space-y-2">
          <div>
            <label className="block font-medium text-gray-700">
              Número de Documento
            </label>
            <input
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
            <label className="block font-medium text-gray-700">Nombre</label>
            <input
              {...register("nombre")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
              placeholder="Ej: Juan"
            />
            {errors.nombre && (
              <p className="text-red-500 text-sm">{errors.nombre.message}</p>
            )}
          </div>

          <div>
            <label className="block font-medium text-gray-700">Apellido</label>
            <input
              {...register("apellido")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
              placeholder="Ej: Pérez"
            />
            {errors.apellido && (
              <p className="text-red-500 text-sm">{errors.apellido.message}</p>
            )}
          </div>

          <div>
            <label className="block font-medium text-gray-700">Email</label>
            <input
              {...register("email")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
              placeholder="Ej: juanperez@gmail.com"
              type="email"
            />
            {errors.email && (
              <p className="text-red-500 text-sm">{errors.email.message}</p>
            )}
          </div>

          <input
            type="text"
            {...register("rol")}
            value="USER"
            disabled
            hidden
          />

          <div>
            <label className="block font-medium text-gray-700">
              Contraseña
            </label>
            <input
              {...register("contrasena")}
              type="password"
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
            />
            {errors.contrasena && (
              <p className="text-red-500 text-sm">
                {errors.contrasena.message}
              </p>
            )}
          </div>

          <div className="space-y-2 mt-2">
            <label className="block font-medium text-gray-700">
              Confirmar Contraseña
            </label>
            <input
              type="password"
              {...register("confirmPassword")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
            />
            {errors.confirmPassword && (
              <p className="text-red-500 text-sm">
                {errors.confirmPassword.message}
              </p>
            )}
          </div>

          {successMessage && <p className="text-green-600">{successMessage}</p>}
          {errorMessage && <p className="text-red-600">{errorMessage}</p>}
          <div className="mt-4 flex justify-end gap-4">
            <button
              type="button"
              onClick={() => reset()}
              className="px-4 py-2 bg-red-500 text-white rounded"
            >
              Borrar
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              Registrar
            </button>
          </div>
        </div>
      </div>
    </form>
  );
}
