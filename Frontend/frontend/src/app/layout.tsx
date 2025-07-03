import { getTokenData } from "@/lib/auth"; // función que decodifica el JWT
import Sidebar from "@/components/Sidebar";
import "./globals.css";

export default async function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const tokenData = getTokenData(); // Podés obtener esto del middleware o cookies

  return (
    <html lang="es">
      <body className="flex">
        {tokenData ? <Sidebar role={tokenData.rol} /> : null}
        <main className="flex-1 p-4">{children}</main>
      </body>
    </html>
  );
}
