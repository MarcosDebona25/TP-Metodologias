import { z } from "zod";

export const userFormSchema = z
  .object({
    numeroDocumento: z
      .string()
      .length(8, "Debe tener 8 dígitos")
      .regex(/^\d+$/, "Debe ser numérico"),
    nombre: z.string().min(1, "Requerido"),
    apellido: z.string().min(1, "Requerido"),
    email: z.string().email("Email inválido"),
    rol: z.enum(["ADMIN", "USER"], { message: "Rol inválido" }),
    contrasena: z.string().min(6, "Mínimo 6 caracteres"),
    confirmPassword: z.string(),
  })
  .refine((data) => data.contrasena === data.confirmPassword, {
    path: ["confirmPassword"],
    message: "Las contraseñas no coinciden",
  });

export type UserFormSchema = z.infer<typeof userFormSchema>;
