import React, { Component } from 'react'
import Cinema from './Cinema'

export default class City extends Component {
    constructor(props) {
        super(props)
    
        this.state = {
            city:null,
            cinemas:[],
            currentCinema:null
        }
    }

    updateState() {
        let cines = this.props.city.cinemas;
        this.setState({
            city:this.props.city,
            cinemas:cines,
            currentCinema:cines[0]
        })
    }

    componentDidMount() {
        this.updateState();
    }

    componentDidUpdate(prevProps, prevState) {

        if(prevProps === undefined || this.state.city==null) {
            return false
        }

        if(this.state.city.id !== this.props.city.id) {
            this.updateState();
        }
     }

    setCurrentCinema(cine) {
        this.setState({
            currentCinema:cine
        })
    }
    
    render() {
        return (
            <div className="card">
                <h5 className="card-header">
                    Listes des cinemas de <spane className="">{this.state.city==null?"":this.state.city.name}</spane>
                </h5>
                <div className="card-body">
                    {
                        this.state.cinemas.length > 0 ? '' : <h5>Cette ville ne contient pas de cinéma pour l'instant</h5>
                    }
                    <div className="row">
                        {
                            this.state.cinemas.map((cinema, index) => 
                                <div className="m-2" key={index}>
                                    <button onClick={()=>this.setCurrentCinema(cinema)}
                                        className={cinema===this.state.currentCinema?
                                            "btn btn-success text-center":
                                            "btn btn-outline-success text-center"}>
                                        {cinema.name}
                                    </button>
                                </div>
                            )
                        }
                    </div>
                    <div className="row mt-4">
                        {
                            this.state.currentCinema==null?"":
                                <Cinema cinema={this.state.currentCinema}/>
                        }
                    </div>
                </div>
            </div>
        )
    }
}
