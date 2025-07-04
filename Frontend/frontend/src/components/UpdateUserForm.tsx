"use client";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { userFormSchema, UserFormSchema } from "@/schemas/userSchema";
import { User } from "@/types/User";
import { getUserByNumeroDocumento, updateUser } from "@/services/userService";

export default function UpdateUserForm() {
  const [userFound, setUserFound] = useState<User | null>(null);
  const [searchDni, setSearchDni] = useState("");
  const [error, setError] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
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

  const fetchUser = async () => {
    setError(null);
    try {
      const data = await getUserByNumeroDocumento(searchDni);
      setUserFound(data);
      reset({
        numeroDocumento: data.numeroDocumento,
        nombre: data.nombre,
        apellido: data.apellido,
        email: data.email,
        rol: "USER",
        contrasena: "",
        confirmPassword: "",
      });
    } catch (err) {
      if (err instanceof Error) {
        setError("Usuario no encontrado");
      }
    }
  };

  const onSubmit = async (data: UserFormSchema) => {
    try {
      await updateUser(data);
      alert("Usuario actualizado exitosamente");
      setUserFound(null);
      reset();
    } catch (err) {
      if (err instanceof Error) {
        alert(err.message || "Error al actualizar usuario");
      }
    }
  };
  const router = useRouter();

  useEffect(() => {
    const rol = localStorage.getItem("rol");
    if (rol !== "ADMIN" && rol !== "USER") {
      router.push("/");
    }
  }, [router]);
  return (
    <div className="max-w-xl mx-auto p-4 border rounded shadow">
      <h2 className="text-2xl font-bold mb-4 text-gray-800">
        Modificar Usuario
      </h2>

      <div className="space-y-2 mb-4">
        <label className="block font-medium text-gray-700">
          Buscar por DNI
        </label>
        <div className="flex gap-2">
          <input
            type="text"
            value={searchDni}
            onChange={(e) => setSearchDni(e.target.value)}
            className="w-full rounded border border-gray-300 text-gray-800 bg-white"
            placeholder="Ingrese número de documento"
          />
          <button
            onClick={fetchUser}
            className="bg-blue-600 text-white px-4 py-2 rounded"
          >
            Buscar
          </button>
        </div>
        {error && <p className="text-red-500">{error}</p>}
      </div>

      {userFound && (
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <div hidden>
            <label className="block text-gray-700 font-medium">
              Número de Documento
            </label>
            <input
              {...register("numeroDocumento")}
            />
          </div>

          <div>
            <label className="block text-gray-700 font-medium">Nombre</label>
            <input
              {...register("nombre")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
            />
            {errors.nombre && (
              <p className="text-red-500 text-sm">{errors.nombre.message}</p>
            )}
          </div>

          <div>
            <label className="block text-gray-700 font-medium">Apellido</label>
            <input
              {...register("apellido")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
            />
            {errors.apellido && (
              <p className="text-red-500 text-sm">{errors.apellido.message}</p>
            )}
          </div>

          <div>
            <label className="block text-gray-700 font-medium">Email</label>
            <input
              type="email"
              {...register("email")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
            />
            {errors.email && (
              <p className="text-red-500 text-sm">{errors.email.message}</p>
            )}
          </div>

          <div hidden>
            <label className="block text-gray-700 font-medium">Rol</label>
            <input
              {...register("rol")}
            />
            {errors.rol && (
              <p className="text-red-500 text-sm">{errors.rol.message}</p>
            )}
          </div>

          <div>
            <label className="block text-gray-700 font-medium">
              Nueva Contraseña (opcional)
            </label>
            <input
              type="password"
              {...register("contrasena")}
              className="w-full rounded border border-gray-300 text-gray-800 bg-white"
            />
          </div>

          <div>
            <label className="block text-gray-700 font-medium">
              Repetir Contraseña
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

          <div className="flex justify-end gap-4 mt-4">
            <button
              type="button"
              onClick={() => {
                reset();
                setUserFound(null);
              }}
              className="bg-red-500 text-white px-4 py-2 rounded"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="bg-blue-600 text-white px-4 py-2 rounded"
            >
              Actualizar
            </button>
          </div>
        </form>
      )}
    </div>
  );
}
