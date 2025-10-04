import { useState } from "react"
import {InputBox} from './components'
import useCurrencyInfo from "./hooks/userCurrencyInfo"


function App() {
  const [amount, setAmount] = useState(0)
  const [from, setForm] = useState("usd")
  const [to, setTo] = useState("inr")
  const [convertedAmount, setConvertedAmount] = useState(0)
  const currencyInfo = useCurrencyInfo(from)
  const options = Object.keys(currencyInfo)
  const swap = () => {
    setForm(to)
    setForm(from)
    setConvertedAmount(amount)
    setAmount(convertedAmount)
  }

  const convert = () => {
    setConvertedAmount(amount * currencyInfo[to])
  }

  return (
  <div
            className="w-full h-screen flex flex-wrap justify-center items-center bg-cover bg-no-repeat"
            style={{
                backgroundImage: `url('https://media.istockphoto.com/id/1399575283/th/%E0%B8%A3%E0%B8%B9%E0%B8%9B%E0%B8%96%E0%B9%88%E0%B8%B2%E0%B8%A2/%E0%B9%80%E0%B8%87%E0%B8%B4%E0%B8%99%E0%B8%97%E0%B9%8D%E0%B8%B2%E0%B9%83%E0%B8%AB%E0%B9%89%E0%B9%82%E0%B8%A5%E0%B8%81%E0%B9%84%E0%B8%9B%E0%B8%A3%E0%B8%AD%E0%B8%9A-%E0%B9%86.jpg?s=1024x1024&w=is&k=20&c=8H0hknVAkZwFic7Q0_DEVwKw5fzjNYgBvv-ExdWp234=')`,
            }}
        >
            <div className="w-full">
                <div className="w-full max-w-md mx-auto border border-gray-60 rounded-lg p-5 backdrop-blur-sm bg-white/30">
                    <form
                        onSubmit={(e) => {
                            e.preventDefault();
                            convert()
                           
                        }}
                    >
                        <div className="w-full mb-1">
                            <InputBox
                                label="From"
                                amount={amount}
                                currencyOptions={options}
                                onCurrencyChange= { (currency) => setAmount(amount) }
                                  selectCurrency={from}
                                  onAmountChange={(amount) => setAmount(amount)}
                            />
                        </div>
                        <div className="relative w-full h-0.5">
                            <button
                                type="button"
                                className="absolute left-1/2 -translate-x-1/2 -translate-y-1/2 border-2 border-white rounded-md bg-blue-600 text-white px-2 py-0.5"
                                onClick={swap}
                            >
                                swap
                            </button>
                        </div>
                        <div className="w-full mt-1 mb-4">
                            <InputBox
                                label="To"
                                 amount={convertedAmount}
                                currencyOptions={options}
                                onCurrencyChange= { (currency) => setTo(currency) }
                                  selectCurrency={to}
                                 amountDisable
                            />
                        </div>
                        <button type="submit" className="w-full bg-blue-600 text-white px-4 py-3 rounded-lg">
                            Convert {from.toUpperCase()} to {to.toLocaleUpperCase()}
                        </button>
                    </form>
                </div>
            </div>
        </div>

  )
}

export default App


