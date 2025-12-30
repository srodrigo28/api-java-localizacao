2️⃣ FRONT-END — Next.js (React)
📁 Estrutura simples
app/page.tsx

📄 page.tsx
```
"use client"

import { useState } from "react"

export default function Home() {
  const [distance, setDistance] = useState<number | null>(null)
  const [loading, setLoading] = useState(false)

  async function getLocationAndCalculate() {
    setLoading(true)

    navigator.geolocation.getCurrentPosition(async (pos) => {
      const userLat = pos.coords.latitude
      const userLng = pos.coords.longitude

      // destino fixo (ex: empresa, loja, etc.)
      const targetLat = -23.561684
      const targetLng = -46.625378

      const response = await fetch("http://localhost:8080/distance", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          userLat,
          userLng,
          targetLat,
          targetLng,
        }),
      })

      const data = await response.json()
      setDistance(data.distanceKm)
      setLoading(false)
    })
  }

  return (
    <main style={{ padding: 40 }}>
      <h1>Calcular Distância</h1>

      <button onClick={getLocationAndCalculate}>
        Calcular distância
      </button>

      {loading && <p>Calculando...</p>}

      {distance && (
        <p>Distância: <strong>{distance} km</strong></p>
      )}
    </main>
  )
}
```