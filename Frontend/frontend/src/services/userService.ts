import { NextResponse } from "next/server";
import { UserFormSchema } from "@/schemas/userSchema";
import { User } from "@/types/User";

export async function getUserByNumeroDocumento(dni: string): Promise<User> {
  const response = await fetch(`http://localhost:8080/api/usuarios/${dni}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    throw new Error("Usuario no encontrado");
  }

  return await response.json();
}

export async function updateUser(data: UserFormSchema): Promise<void> {
  const response = await fetch(
    `http://localhost:8080/api/usuarios/${data.numeroDocumento}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }
  );

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || "Error al actualizar el usuario");
  }
}

export async function submitUser(data: UserFormSchema): Promise<void> {
  const response = await fetch(`http://localhost:8080/api/usuarios`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || "Error creando usuario");
  }
}
