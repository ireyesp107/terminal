# CS0320 Sprint 3: Command Terminal Webapp Assignment

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

#### 


### Description
For this project, I integrated the front and back-end we developed. I converted previous project to use the React framework seen. Overall, this Terminal is a web app made with a variety of users in mind end-user stakeholders and front-end developer stakeholders as well. For example, it allows end-user stakeholders to get a CSV file's contents as well as the stats regarding the number of rows and columns in that CSV data. Additionally, it allows end-user stakeholder to retrieve the current temperature for a location. Finally, it permits a front-end developer stakeholder to register new commands by providing a function to run whenever the command is seen. As software engineers, we also accounted for detailed documentation as well as making sure that a screen reader can handle reading these results, that way any and all users of our program can easily access and understand it.

## Design Descisions 
We have terminal file which constructs the whole framework of the app. The REPLCommands file defines an interface for a REPL command and defines the functions for each command. Here you can add more commands and their functions. 

## How to run:

###THE BACK END SERVER: 
To run my back-end server, run the Server class (click the green arrow in the top right corner). 
This will start our (back-end) API server.

## In the project directory, you can run:

### `npm install && npm start`

Once both our front-end and our back-end projects are running (see below to run these) the user may enter three different commands into our React page command line:

get csv-file-path

get 

weather latitude coordinate longitude coordinate

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.\
You will also see any lint errors in the console

### `npm test`

Launches the test runner in the interactive watch mode.
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature. However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).
