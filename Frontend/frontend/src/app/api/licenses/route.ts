import { NextResponse } from "next/server";

export async function POST(req: Request) {
  const data = await req.json();
  console.log("Received license submission:", data);
  
  // Here you would call your real backend (SpringBoot) in the future

  return NextResponse.json({ message: "License created successfully" }, { status: 201 });
}
