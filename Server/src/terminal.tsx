import { useState, Dispatch, SetStateAction } from 'react';
import './styles/terminal.css'
import { getCSV,getStats, weather,REPLFunction } from './ReplCommands';
import {validCommands} from './ReplCommands';


export const TEXT_command_input_placeholer = "Input a command here."
export const TEXT_submit_button_text = "Submit!"
export const TEXT_submit_button_accessible_name = "Click to Submit Your Command."
export const TEXT_input_command_accessible_name = 'Inputted Command.'
export const TEXT_history = "Command history of past command inputs and their outputs."
export const TEXT_output_command_accessible_name = 'Command Output.'
let output: string = ""

//weather 39.7456 -97.0892 for weather
//get data/aeneidData.csv for data testing

/**
 * Register a new command by adding them to the map like below (for get, stats etc.), 
 * you will need to also define a corresponding function in ReplCommands.tsx. 
 */
// let validCommands: Map<string,REPLFunction> = new Map()
// validCommands.set("get", getCSV)
// validCommands.set("stats", getStats)
// validCommands.set("weather", weather)

export function addNewCommand(commandName: string, func: REPLFunction){
  validCommands.set(commandName, func)
}


/**
 * A method that parses a command, 
 * 
 * @param input  a string representing the user's input into the terminal.  
 * @return output  a string of the output of the command to be displayed.
 */
async function parseCommand(input: string|undefined) {

  if (input !==undefined){
      let args: string[] = []
      args=input.split(' ')
      console.log(validCommands.get(args[0]))

    if (isValidCommand(args[0]) && args[0] !==undefined) {
      let replFunction : REPLFunction|undefined = validCommands.get(args[0])
      args = args.splice(1,args.length)

      if(replFunction !== undefined){
      output = await replFunction(args)}

    } else { //if repl function is undefined
      output = (`Command ${args[0]} not supported`)  
    }
  
  } else { //if the input is undefined 
    output=(`Not input`)
  }
  console.log("Test")
console.log(output)
}

/**
 * A helper method that checks if the inputted command is valid. That is, if 
 * it is part of the commands map and thus has a correspinding REPL function.  
 * 
 * @param command  a string of the inputted command.  
 * @return boolean  if command is valid and defined.
 */
function isValidCommand(command : string) : boolean {
  
  if(command !== undefined && validCommands.has(command)){
    console.log("Command is found in Commands ")
      return true
  }
  return false 
}

/**
 * Parameters for the Controlled Input. 
 * 
 * @param input  a string of the inputted command. 
 * @param setValue  a function that sets the state of the inputted string
 * @param ariaLabel  an string which labels the input box 
 */
interface ControlledInputProps {
  input: string, 
  setInput: Dispatch<SetStateAction<string>>,
  ariaLabel: string 
}

/**
 * A method that checks if the inputted command is valid. That is, if 
 * it is part of the commands map and thus has a correspinding REPL function.  
 * 
 * @param ControlledInputProps  state variable of the inputted string, it's setter, and an 
 *                              aria label. 
 * @return an input JSX element which represents the input box. 
 */
function ControlledInput({input: value, setInput: setValue, ariaLabel}: ControlledInputProps) { 
  return (
    <input 
      value={value} 
      onChange={(ev) => setValue(ev.target.value)}
      aria-label={ariaLabel}
    ></input>
  );
}

/**
 * Parameters for a new command. 
 * 
 * @param addCommand  a function that adds the command and its result to an array
 * @param setNotification  a function that sets the state of an alert to the user.
 */
interface NewCommandProps {
  addCommandResult: (command: string[]) => any,
  setNotification: Dispatch<SetStateAction<string>>
}

/**
 * Returns a div representing the command input section of the webpage. 
 * 
 * @param NewCommandProps  contains a function that adds the command and its result to an array and 
 *                         a function that sets the state of an alert to the user.
 */
function NewCommand({addCommandResult: addCommandResult, setNotification}: NewCommandProps) {
  const [myInput, setMyInput] = useState<string>('');

  return (
    <div className="new-command">
      <div className="current - command"> 
      <fieldset>
        <legend>Enter a Command:</legend>
        <ControlledInput input={myInput} setInput={setMyInput} 
        ariaLabel={TEXT_command_input_placeholer}/> 
      </fieldset>   
      </div>
      <div>
        <button onClick={() => {  
            if(myInput !==null) {
              parseCommand(myInput)
              setTimeout(()=>{
                console.log("Output is " + output)
                addCommandResult([myInput, output])
              },500)

              setMyInput('') //Clear the input box
              setNotification('') //Clear notification
              output = "No Output Found. Please provide a valid command."
            } else {
              setNotification('Please provide a command.')
            }
          }}
          aria-label={TEXT_submit_button_accessible_name}>
          
          {/* The text displayed on the button */}
          {TEXT_submit_button_text}
        </button>
      </div>
    </div>
  );  
}

/**
 * Parameters for a new command. 
 * 
 * @param commandHistory an array representing the command history, with each command 
 *                       and its result  
 */
 interface OldRoundProps {
  commandHistory: string[],
}

/**
 * Returns a div representing the command history section of the webpage. 
 * 
 * @param OldRoundProps  contains an array representing the command history, 
 *                       with each command and its result. 
 */
function OldRound( {commandHistory}: OldRoundProps) {
  return (
    <div className={"repl-history"}
         aria-label={TEXT_history}> 
      <div aria-label={TEXT_input_command_accessible_name + commandHistory[0]}>
          <p><b>Command:</b> {commandHistory[0]}</p>
      </div>
      <div aria-label={TEXT_output_command_accessible_name + commandHistory[1]}> 
          <p><b>Output:</b> {commandHistory[1]}</p> 
      </div>
    </div>
  );  
}

/**
 * Constructs and returns a div representing the overarching framework of the webpage:
 * the space for inputting a new command and the space for representing the command history. 
 * 
 * @return a div representing the overarching framework of the webpage
 */
export default function Terminal() {
  const [commandHistory, setCommandHistory] = useState<string[][]>([]);
  const [notification, setNotification] = useState('');

  return (
  <div className="App">   
    <NewCommand         
        setNotification={setNotification}
        //react state updates and makes a new round an old round.
        addCommandResult={(newValidCommand: string[]) => {  
          const newCommandHistory = commandHistory.slice()
          console.log("end input: " + newValidCommand)
          newCommandHistory.push(newValidCommand)
          setCommandHistory(newCommandHistory)
          }
        }/>
        {commandHistory.map((history,index) =>
      (<OldRound commandHistory={history} key={index} /> ))}
        {notification}  
  </div>);
  }


