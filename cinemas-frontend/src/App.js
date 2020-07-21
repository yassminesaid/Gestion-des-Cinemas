import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Route, Switch, Link, BrowserRouter as Router} from 'react-router-dom'
import Home from './Components/Home';


function App() {
    return (
        <Router>
          <nav className="navbar navbar-expand-lg navbar-light bg-light" style={{height:80 + 'px'}}>
            <ul className="navbar-nav col">
              <li>
                <Link className="navbar-brand" to="/home">Cinema Hub</Link>
              </li>
            </ul>
            <ul class="nav nav-tabs card-header-tabs justify-content-end col-auto">
              <li class="nav-item">
                <a className="nav-link text-success"><b>Home</b></a>
              </li>
              <li class="nav-item">
                <a class="nav-link text-success"><b>Contact US</b></a>
              </li>
              <li class="nav-item">
                <a class="nav-link text-success"><b>About US</b></a>
              </li>
            </ul>
          </nav>
          <div className="m-2 mt-4">
            <Switch>
              <Route path="/" component={Home}></Route>
              <Route path="/home" component={Home}></Route>
            </Switch>
          </div>
        </Router>
    );
}

export default App;
