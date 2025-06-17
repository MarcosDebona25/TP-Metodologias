// schemas/personSchema.ts
import { z } from "zod";

export const personFormSchema = z.object({
  idNumber: z
    .string()
    .length(8, { message: "El número de DNI debe tener 8 dígitos" })
    .regex(/^\d+$/, { message: "Solo se permiten números" }),

  firstName: z
    .string()
    .min(1, { message: "El nombre es obligatorio" })
    .max(50, { message: "El nombre es demasiado largo" }),

  lastName: z
    .string()
    .min(1, { message: "El apellido es obligatorio" })
    .max(50, { message: "El apellido es demasiado largo" }),

  dateOfBirth: z.string().refine((val) => !isNaN(Date.parse(val)), {
    message: "Fecha inválida",
  }),

  address: z
    .string()
    .min(5, { message: "La dirección es muy corta" })
    .max(100, { message: "La dirección es demasiado larga" }),

  bloodType: z.enum(["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"], {
    errorMap: () => ({ message: "Selecciona un grupo sanguíneo válido" }),
  }),

  donor: z.boolean(),
});

export type PersonFormSchema = z.infer<typeof personFormSchema>;
