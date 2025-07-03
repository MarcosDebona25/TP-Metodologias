// schemas/loginSchema.ts
import { z } from "zod";

export const loginSchema = z.object({
  numeroDocumento: z
    .string()
    .min(8, "Debe tener al menos 8 dígitos")
    .max(8, "Debe tener 8 dígitos")
    .regex(/^\d+$/, "Debe contener solo números"),
  contrasena: z
    .string()
    .min(6, "La contraseña debe tener al menos 6 caracteres"),
});

export type LoginSchema = z.infer<typeof loginSchema>;
