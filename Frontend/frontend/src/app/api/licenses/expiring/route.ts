import { NextResponse } from "next/server";

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const from = searchParams.get("from");
  const to = searchParams.get("to");

  // Optional: simulate delay
  await new Promise((res) => setTimeout(res, 500));

  // Simulated data
  const data = [
    {
      numeroDocumento: "12345678",
      clases: ["A", "B"],
      fechaEmision: "2021-06-01",
      fechaVencimiento: "2024-06-25",
    },
    {
      numeroDocumento: "87654321",
      clases: ["C"],
      fechaEmision: "2021-07-01",
      fechaVencimiento: "2024-06-28",
    },
    {
      numeroDocumento: "11223344",
      clases: ["D", "E"],
      fechaEmision: "2020-06-15",
      fechaVencimiento: "2024-07-01",
    },
  ];

  // Optional: filter based on query range (string comparison works for yyyy-mm-dd)
  const filtered = data.filter(
    (item) => item.fechaVencimiento >= from! && item.fechaVencimiento <= to!
  );

  return NextResponse.json(filtered);
}
