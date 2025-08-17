import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import Card from './component/Card'

function App() {
  const [count, setCount] = useState(0)
  let myObj = {
    username: "Abinash",
    age: 30
  }
  let newArray = [1,2,3]
  return (
    <>
    <h1 className='bg-green-400 text-black p-4 
    rounded-lg'>Tailwind test</h1>

    <Card username ="1:First Panel" probObject={newArray} />
    <Card username ="2: Second Panel"/>

    </>
  )
}

export default App
