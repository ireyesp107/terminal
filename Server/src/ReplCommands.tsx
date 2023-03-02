export let getCSV: REPLFunction
export let weather: REPLFunction
export let getStats: REPLFunction
export let newStats:REPLFunction


var rows: number
var columns: number
let canCallStats: boolean=false

/**
 * Register a new command by adding them to the map like below (for get, stats etc.), 
 * you will need to also define a corresponding function in ReplCommands.tsx. 
 */


/**
 * An interface for a repl command's function. 
 * 
 * @param args  an array of the inputted command. 
 * @return Promise<string>|string  a string of the output of the command to be displayed.
 */
export interface REPLFunction{
    (args: Array<string>): Promise<string>|string,
}

/**
 * A REPLFunction for the 'get' command. It communitcates with the server to parse
 * the inputted file and also calls the findStats helper to get the rows and columns of the 
 * CSV, and then it sets canCallStats as true (as the user has now inputted a file), finally it 
 * returns the parsed CSV as a string.
 * 
 * @param args  an array of strings that represent the whole user input. TODO 
 * @return Promise<string> the output to be displayed to the user. 
 */
getCSV = async function handleGet(args: string[]): Promise<string> {
    var value = undefined
    if(args.length==1){
    await fetch('http://localhost:3232/loadcsv?filepath=' + args[0])
    .then((r) => r.json())
    value = await fetch('http://localhost:3232/getcsv')
    .then((r) => r.json())
    .then((r) => value = r.data)
    .catch((e) => value = "Unable to access file")
    if(value===undefined){
        return "couldn't access the file"
    }
    else{
    findStats(value)    
    canCallStats=true
    return value.toString();
    }
    }
    else{
        value="You have too many inputs"
        return value
    }
}


/**
 * A helper function that calculates the rows and columns of the CSV file.
 * 
 * @param args  an array of strings representing the CSV.  
 */
function findStats(args: string[]){
    if(args.length===0){
        rows=0
        columns=0
    }
    else{
    rows=args.length
    columns=args[0].length 
    }
}


/**
 * A REPLFunction for the 'stats' command. It returns the rows and columns or an error message. 
 * 
 * @param args  an array of strings that represent the whole user input. TODO 
 * @return string  the output to be displayed to the user. The rows and columns 
 *                 or an error message. 
 */
getStats = function handleStats(args: string[]): string {
    if(canCallStats){
    return "rows: "+ rows + " cols: " + columns;
} else {
    return "Make sure to load a file before entering the stats command.";
    }
}
//newStats = async function handleStats(args: string[]): string {

//}

/**
 * A REPLFunction for the 'weather' command. It communicates with the server to retrieve the weather 
 * for the inputted longitude and latitude points. 
 * 
 * @param points  an array of strings that represent the user's inputted latitude & longitude points for 
 *                which the whether will be retrieved. 
 * @return Promise<string>  the output to be displayed to the user. The tempreature or an error message. 
 */
weather = async function weatherHandler(points: string[]): Promise<string> {
    return new Promise((resolve) => {
    if(points.length!==2){
      resolve('Invalid number of arguments. Please input a latitude and a longitude point.')
    }
    else if (points.length ===2){
      const weatherURL = `http://localhost:3232/weather?lat=${points[0]}&lon=${points[1]}`
      console.log(weatherURL)
            return fetch(weatherURL)
                .then(response => response.json())
                .then(response => {
                  console.log(response.result)
                    if (response.result === `success`) {
                        console.log(`Success, got temperature`)
                        resolve(`Temperature at ${points[0]}, ${points[1]} is ${response.temperature}`)
                    }
                    else {
                        resolve(`Weather for ${points[0]}, ${points[1]} could not be found.
                         Please check your coordinates and try again.`)
                    }
                })
            }
        })
    }
    


    export let validCommands: Map<string,REPLFunction> = new Map()
    validCommands.set("get", getCSV)
    validCommands.set("stats", getStats)
    validCommands.set("weather", weather)