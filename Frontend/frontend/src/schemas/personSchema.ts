// schemas/personSchema.ts
import { z } from "zod";

export const personFormSchema = z.object({
  idNumber: z.string().min(8, "ID must be 8 digits").max(8).regex(/^\d+$/, "Must be numeric"),
  firstName: z.string().min(1, "Required"),
  lastName: z.string().min(1, "Required"),
  dateOfBirth: z.string(),
  address: z.string().min(1, "Required"),
  bloodType: z.string(),
  donor: z.boolean(),
});

export type PersonFormSchema = z.infer<typeof personFormSchema>;
