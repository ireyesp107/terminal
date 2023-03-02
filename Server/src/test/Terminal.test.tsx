// jest-dom adds custom jest matchers for asserting on DOM nodes.
// allows you to do things like:
// expect(element).toHaveTextContent(/react/i)
// learn more: https://github.com/testing-library/jest-dom
import '@testing-library/jest-dom';
import { render, screen } from '@testing-library/react';
import Terminal, { addNewCommand, TEXT_command_input_placeholer, TEXT_history, TEXT_submit_button_accessible_name } from '../terminal';
import userEvent from '@testing-library/user-event';
import {REPLFunction, validCommands} from '../ReplCommands';

/**
 * weather 41.8268 -71.4029
 * get data/aeneidData.csv
 * stats
 */ 
/**
 * Renders a "new" Terminal before each test.
 */
 describe('Terminal Testing Suite', () => {
  beforeEach(() => {
    render(<Terminal/>);
  });

  /**
   * In this test, we check to make sure that the input box and button 
   * are appearing on the webpage. 
   */
  test('confirming basic elements are rendered', () => {
    const inputBox = screen.getByRole("textbox", { name: TEXT_command_input_placeholer})
    const button = screen.getByRole("button", { name: TEXT_submit_button_accessible_name})
    expect(inputBox).toBeInTheDocument
    expect(button).toBeInTheDocument
  })
});

//Test repl history displays 
test('renders history', () => {
  const input = screen.getByRole("textbox", {name: TEXT_command_input_placeholer})
  const submitButton = screen.getByRole("button", {name: TEXT_submit_button_accessible_name}) 

  const getCommand = "get userData/animal.csv"
  userEvent.type(input, getCommand)
  userEvent.click(submitButton)

  setTimeout(() => {
  const replHistory = screen.getByRole("repl-history", {name: TEXT_history})
  expect(replHistory).toBeInTheDocument()},500)
});


/**
 * In this test, we test to make sure that stats works properly in the case
 * when there is not file loaded. In other words, we expect it to give the
 * user a descriptive output message letting them know that a valid get
 * command must be called before calling the stats command. 
 */
  test("stats properly runs when no file loaded", async() => {
  const submitButton = screen.getByRole("button")
  const input = screen.getByRole("textbox")
  userEvent.type(input, "stats")
  userEvent.click(submitButton)
  let command = await screen.findByText("Command: stats")
  let output = await screen.findByText(`Output: Make sure to load a file before entering the stats command.`)
  expect(command).toBeInTheDocument
  expect(output).toBeInTheDocument
})

/**
   * In this test, we test to make sure that stats works properly in the case 
   * when there is an empty csv file. And test the loading of an empty csv. 
   */
test("stats after empty get", async() => {
  const submitButton = screen.getByRole("button")
  const inputBox = screen.getByRole("textbox")

  userEvent.type(inputBox, "get userData/empty.csv")
  userEvent.click(submitButton)

  let command1 = await screen.findByText("Command: get")
  let output1 = await screen.findByText(`Output `)
  expect(command1).toBeInTheDocument
  expect(output1).toBeInTheDocument

  userEvent.type(inputBox, "stats")
  userEvent.click(submitButton)

  let command2 = await screen.findByText("Command: stats")
  let output2 = await screen.findByText(`Output: `) //TODO
  expect(command2).toBeInTheDocument
  expect(output2).toBeInTheDocument
}) 

/**
 * In this test, we test to make sure stats works in the case when it is 
 * called after a get command that results in an error. In other words, we 
 * expect our program to inform the user that they must call a valid (get)
 * command before calling the stats command.
 */
test("stats after an error", async() => {
    const submitButton = screen.getByRole("button")
    const inputBox = screen.getByRole("textbox")
    userEvent.type(inputBox, "get")
    userEvent.click(submitButton)
    
    let command1 = await screen.findByText("Command: get")
    let output1 = await screen.findByText(`Output: No Output Found. Please provide a valid command.`)
    expect(command1).toBeInTheDocument
    expect(output1).toBeInTheDocument

    userEvent.type(inputBox, "stats")
    userEvent.click(submitButton)

    let command2 = await screen.findByText("Command: stats")
    let output2 = await screen.findByText(`Output: Make sure to load a file before entering the stats command.`)
    expect(command2).toBeInTheDocument
    expect(output2).toBeInTheDocument
})

/**
 * In this test, we test the basic functionality of the get command.
 * Essentially, we just make sure that we can get the contents of a 
 * simple, one-lined csv file. 
 */
test("get command properly runs", async() => {
    const submitButton = screen.getByRole("button")
    const input = screen.getByRole("textbox")
    userEvent.type(input, "get userData/oneLine.csv")
    userEvent.click(submitButton)

    let command = await screen.findByText("Command: get userData/oneLine.csv")
    let output = await screen.findByText(`Output: this is a test hello`)
    expect(command).toBeInTheDocument
    expect(output).toBeInTheDocument
})

/**
 * In this test, we test to make sure that the weather command properly
 * functions in a "basic" case. In other words, we check to make sure that 
 * given the user's formatting is correct and the loaction they input is
 * within the United States of America, it should return the correct,
 * respective temperature.
 * 
 * IMPORTANT NOTE: This will fail in the future because it returns the 
 * temperature at the given location at the specific time I wrote this test.
 */
test("weather command properly runs", async() => {
    const submitButton = screen.getByRole("button")
    const inputBox = screen.getByRole("textbox")
    userEvent.type(inputBox, "weather 41.8268 -71.4029")
    userEvent.click(submitButton)

    let command = await screen.findByText("Command: weather 41.8268 -71.4029")
    let output = await screen.findByText(`Output: It is currently 38 degrees Fahrenheit at 41.8268, -71.4029`)
    expect(command).toBeInTheDocument
    expect(output).toBeInTheDocument
})

/**
 * In this test, we test to make sure that the stats command runs correctly
 * in a "regular" case. In other words, in the case when a "regular" csv
 * file was loaded previously, we check to make sure that the stats command
 * returns the statistics that it should.
 */
test("stats properly runs", async() => {
    const submitButton = screen.getByRole("button")
    const inputBox = screen.getByRole("textbox")
    userEvent.type(inputBox, "get userData/animal.csv")
    userEvent.click(submitButton)
    let command1 = await screen.findByText("Command: get userData/animal.csv")
    let output1 = await screen.findByText(`Output: Animal Name Age Dog Betty 10 Cat Charlie 3 Mouse Earl 1`) //TODO
    expect(command1).toBeInTheDocument
    expect(output1).toBeInTheDocument

    userEvent.type(inputBox, "stats")
    userEvent.click(submitButton)
    let command2 = await screen.findByText("Command: stats")
    let output2 = await screen.findByText(`Output: This CSV contains 4 rows and 3 columns.`) //TODO
    expect(command2).toBeInTheDocument
    expect(output2).toBeInTheDocument
})  

/**
 * In this test, we check to make sure that our stats command functions
 * after 2 get commands. To make sure that after running 
 * back-to-back get commands, that our stats
 * function only returns the statistics of the most recent command. 
 */
test("stats after 2 gets", async() => {
    const submitButton = screen.getByRole("button")
    const inputBox = screen.getByRole("textbox")
    userEvent.type(inputBox, "get userData/empty.csv")

    userEvent.click(submitButton)
    let command1 = await screen.findByText("Command: get userData/empty.csv")
    let output1 = await screen.findByText(`Output:`)
    expect(command1).toBeInTheDocument
    expect(output1).toBeInTheDocument
    
    userEvent.type(inputBox, "get userData/oneLine.csv")
    userEvent.click(submitButton)
    let command2 = await screen.findByText("Command: get userData/oneLine.csv")
    let output2 = await screen.findByText(`Output: this, is, a, test, hello`)
    expect(command2).toBeInTheDocument
    expect(output2).toBeInTheDocument

    userEvent.type(inputBox, "stats")
    userEvent.click(submitButton)
    let command = await screen.findByText("Command: stats")
    let output = await screen.findByText(`Output: rows: 1 cols: 5`)
    
    expect(command).toBeInTheDocument
    expect(output).toBeInTheDocument
})

/**
 * In this test, we check to make sure that our weather command works 
 * correctly in the case when neither lat or lon coordinates are inputted
 * as arguments. We expect, in this case, the command to inform the user 
 * that the 'weather' command needs both a latitude and a longitude
 * coordinate. 
 */
test('weather no lat or lon', async() => {
    const submitButton = screen.getByRole("button")
    const inputBox = screen.getByRole("textbox")
    userEvent.type(inputBox, "weather")
    userEvent.click(submitButton)

    let command = await screen.findByText("Command: weather")
    let output = await screen.findByText(`Output: Invalid number of arguments. Please input a latitude and a longitude point.`)
    expect(command).toBeInTheDocument
    expect(output).toBeInTheDocument
})

/**
 * In this test, we check to make sure that our weather command works 
 * appropriately in the case when only one coordinate is given (i.e.: only
 * lat or lon coordinate is given). In this case, we expect our program to 
 * inform the user that they need to provide valid latitude and longitude
 * coordinates. 
 */
test('weather only one number given', async() => {
    const submitButton = screen.getByRole("button")
    const inputBox = screen.getByRole("textbox")
    userEvent.type(inputBox, "weather 41.8268")
    userEvent.click(submitButton)

    let command = await screen.findByText("Command: weather 41.8268")
    let output = await screen.findByText(`Output: Invalid number of arguments. Please input a latitude and a longitude point.`)
    expect(command).toBeInTheDocument
    expect(output).toBeInTheDocument
})

/**
 * In this test, we check the case where an invalid lat and lon are input
 * into our weather command (that is, they are not valid coordinates with 
 * respect to the United States of America). In this case, we expect our
 * program to direct the user to providing coordinates that exist in the NWS
 * database.
 */
test('weather wrong lat or lon', async() => {
    const submitButton = screen.getByRole("button")
    const input = screen.getByRole("textbox")
    userEvent.type(input, "weather 1 2")
    userEvent.click(submitButton)

    let command = await screen.findByText("Command: weather 1 2")
    let output = await screen.findByText(`Output: Weather for 1, 2 could not be found. Please check your coordinates and try again.`)
    expect(command).toBeInTheDocument
    expect(output).toBeInTheDocument
})

/**
 * In this test, we check to make sure that our get command functions 
 * appropriately in the case when it is "asked" to get an invalid filepath 
 * (that is, it's an invalid filepath name).
 */
test('get wrong filepath', async() => {
    const submitButton = screen.getByRole("button")
    const input = screen.getByRole("textbox")
    userEvent.type(input, "get nonexistent")
    userEvent.click(submitButton)

    let command = await screen.findByText("Command: get nonexistent")
    let output = await screen.findByText("Output: No Output Found. Please provide a valid command.")
    expect(command).toBeInTheDocument
    expect(output).toBeInTheDocument
})

/**
 * In this test, we check to make sure that our program functions in the
 * case when there is noting inputted into the terminal (and the submit 
 * button is clicked). In this case, we expect our program to inform our
 * user that it must provide an existing command.
 */
test('no entry', async() => {
    const submitButton = screen.getByRole("button")
    userEvent.click(submitButton)

    let output = await screen.findByText(`Please provide a command.`)
    expect(output).toBeInTheDocument
})

/**
 * Testing to see if a user can input their new function
 */
let newFunction:REPLFunction
newFunction = function newFunction(args: string[]): string {
 return("hi")
  }

test('can add new command', () => {
  addNewCommand("newFunc", newFunction)
  validCommands.has('newFunc')
})