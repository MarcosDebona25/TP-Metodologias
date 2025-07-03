"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { loginSchema, LoginSchema } from "@/schemas/loginSchema";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function LoginPage() {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting, isValid },
  } = useForm<LoginSchema>({
    resolver: zodResolver(loginSchema),
    mode: "onChange", // 👈 importante para que isValid se actualice dinámicamente
  });

  const [serverError, setServerError] = useState("");
  const router = useRouter();

  const onSubmit = async (data: LoginSchema) => {
    try {
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });

      if (!response.ok) throw new Error("Credenciales inválidas");

      const { token, rol } = await response.json();
      localStorage.setItem("token", token);
      localStorage.setItem("rol", rol);
      router.push("/licenses/new");
    } catch (err: any) {
      setServerError(err.message);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <h1 className="text-4xl font-bold text-gray-800 mt-10 text-center">
        Gestor de Licencias
      </h1>
      <div className="max-w-md mx-auto mt-10 p-6 border rounded shadow space-y-4 align-middle">
        <h2 className="text-2xl font-bold text-gray-800 mb-4 text-center">
          Iniciar Sesión
        </h2>

        <input
          type="text"
          placeholder="DNI"
          {...register("numeroDocumento")}
          className="w-full p-2 rounded border border-gray-300 text-gray-800 bg-white"
        />
        {errors.numeroDocumento && (
          <p className="text-red-500 text-sm">
            {errors.numeroDocumento.message}
          </p>
        )}

        <input
          type="password"
          placeholder="Contraseña"
          {...register("contrasena")}
          className="w-full p-2 rounded border border-gray-300 text-gray-800 bg-white"
        />
        {errors.contrasena && (
          <p className="text-red-500 text-sm">{errors.contrasena.message}</p>
        )}

        {serverError && <p className="text-red-500">{serverError}</p>}

        <button
          type="submit"
          disabled={!isValid || isSubmitting}
          className={`w-full py-2 rounded text-white transition-all ${
            !isValid || isSubmitting
              ? "bg-blue-400 cursor-not-allowed"
              : "bg-blue-600 hover:bg-blue-700"
          }`}
        >
          {isSubmitting ? "Iniciando sesión..." : "Iniciar Sesión"}
        </button>
      </div>
    </form>
  );
}
