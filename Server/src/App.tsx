import './styles/App.css'

import './terminal'
import Terminal from './terminal';
import './GearUp';



/**
 * Constructs and returns a div representing the whole app framework of the webpage:
 * which includes the terminal div.
 * 
 * @return a div representing the app. 
 */
function App() {
  return (
    <div className="App" aria-label='Termnial App'>
      <header className="App-header">
        Terminal App
      </header>
      <Terminal/>
    </div>
  );
}

export default App;
