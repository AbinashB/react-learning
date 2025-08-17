import { useState, Suspense } from "react";

function App() {
  const [count, setCount] = useState(0);

  function handleClick() {
    setCount(c => c + 1);
  }

  return (
    <div>
      <button onClick={handleClick}>Increment</button>
      <Suspense fallback={<p>Loading...</p>}>
        <BigComponent /> {/* React can interrupt this if needed */}
      </Suspense>
      <p>Count: {count}</p>
    </div>
  );
}
