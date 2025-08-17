import './App.css'
import {useState} from 'react'



function App() {


    const [counter, setCounter] = useState(15)

   // let counter = 15;

    const addValue =() =>{

        //counter=counter+1
        if (counter <20 ){
            setCounter(counter+1)
        }
        console.log("C licked  " + counter )
    }

    const removeValue =() =>{

        //counter=counter+1 if
        if (counter >0 ){
            setCounter(counter-1)
           }


        console.log("Clicked  " + counter )
    }

  return (
    <>
     <h1>Hello React </h1>
        <h2>Count value {counter}</h2>
        <button
        onClick={addValue}
        >Add Value</button>
        <br/>
        <button
        onClick={removeValue}
        >Remove Value</button>
        <footer>footer value : {counter} </footer>
    </>
  )
}

export default App
