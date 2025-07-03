import SidebarWrapper from "@/components/SidebarWrapper";

import "./globals.css";

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="es">
      <body className="flex">
        <SidebarWrapper />
        <main className="flex-1 p-4">{children}</main>
      </body>
    </html>
  );
}
