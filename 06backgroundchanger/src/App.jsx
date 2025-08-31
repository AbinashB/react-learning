import { useState } from 'react'


function App() {

  const [color,setColor] = useState("white")

  return (
    <>
   <div className="w-full h-screen duration-10"
   style = {{backgroundColor: color}}> </div>
    <div className='fixed flex justify-center  bottom-12  insert-X-0 px-5'> 

   <div className="flex-warp justify-center gap-3 shadow-lg bg-white rounded-xl px-3 py-2"> 
    <button 
    onClick={()=> setColor("red")}
    className="outline-none px-4 py-1 rounded-full text-white shadow-lg"
    style={{backgroundColor:"red"}}>
     Red 
    </button>
     </div>


  <div className="flex flex-warp justify-center gap-3 shadow-lg bg-white rounded-xl px-3 py-2"> 
    <button 
    onClick={()=> setColor("green")}
    className="outline-none px-4 py-1 rounded-full text-white shadow-lg"
    style={{backgroundColor:"green"}}>
     Green 
    </button>
     </div>



       <div className="flex flex-warp justify-center gap-3 shadow-lg bg-white rounded-xl px-3 py-2"> 
    <button 
    onClick={()=> setColor("blue")}
    className="outline-none px-4 py-1 rounded-full text-white shadow-lg"
    style={{backgroundColor:"blue"}}>
     Blue 
    </button>
     </div>

  


    </div>
    </>
  )
}

export default App
