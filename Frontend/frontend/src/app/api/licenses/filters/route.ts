// Este archivo actúa como proxy entre el frontend y el backend Spring Boot

export async function GET(request: Request) {
    const { searchParams } = new URL(request.url)

    // Convertimos los parámetros de la URL
    const queryString = searchParams.toString()

    // URL del backend (ajustar si lo corrés en otro puerto)
    const backendURL = `http://localhost:8080/api/licencias/filtros?${queryString}`

    try {
        const res = await fetch(backendURL)
        const data = await res.json()

        return new Response(JSON.stringify(data), {
            status: 200,
            headers: {
                "Content-Type": "application/json"
            }
        })
    } catch (error) {
        return new Response(JSON.stringify({ error: "Error al conectar con el backend" }), {
            status: 500
        })
    }
}
