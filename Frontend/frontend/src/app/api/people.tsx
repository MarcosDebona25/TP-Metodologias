// pages/api/people.ts
export default function handler(req, res) {
  res.status(200).json([
    {
    id: "1",
    idType: "DNI",
  idNumber: "12345678",
  firstName: "Juan",
  lastName: "Pérez",
  dateOfBirth: "1990-04-10",
  address: "Pedro de Vega 1234",
  bloodType: "O+",
  donor: true,
    },
    {
    id: "2",
    idType: "DNI",
  idNumber: "98765432",
  firstName: "María",
  lastName: "Gómez",
  dateOfBirth: "1985-04-10",
  address: "Marcial Candioti 1234",
  bloodType: "O+",
  donor: false,
    },
  ]);
}
