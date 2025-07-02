import { NextResponse } from "next/server";
import { UserFormSchema } from "@/schemas/userSchema";

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
