import React, { Component } from 'react'
import TicketsBar from './TicketsBar'
import axios from 'axios'

export default class Salle extends Component {

    constructor(props) {
        super(props)
    
        this.state = {
             salle:null,
             projections:[],
             currentProj:null,
             showTickets:false,
             date:null
        }
    }

    updateState() {
        let s = this.props.salle;
        this.setState({
            salle:s,
            showTickets:false,
            date:this.props.date
        }, () => this.getProjections())
    }

    componentDidMount(){
        this.updateState();
    }

    getProjections() {
        let url = "http://localhost:8085/client/salles/"+this.state.salle.id+"/projections/"
                +this.state.date;
        axios.get(url).then((resp) => {
            let projs = resp.data;
            this.setState({
                projections:projs,
                currentProj:projs[0]
            })
        }).catch(err => {
            console.log(err);
        })
    }

    componentDidUpdate(prevProps, prevState) {

        if(prevProps === undefined || this.state.salle==null) {
            return false
        }

        if((this.state.salle.id !== this.props.salle.id) || this.state.date !== this.props.date) {
            this.updateState();
        }
    }

    setCurrentProj(proj) {
        this.setState({
            currentProj:proj,
            showTickets:true
        })
    }

    render() {
        if(this.state.salle == null) {
            return <div></div>
        }
        else if(this.state.currentProj == null) {
            return (
                <div className="col-lg-6">
                    <div className="card m-1">
                        <h6 className="card-header">
                            Salle {this.props.numero}
                        </h6>
                        <div className="card-body">
                            <h5>Aucune projection dans cette salle pour ce jour</h5>
                        </div>
                    </div>
                </div>
            )
        }
        else return (
            <div className="col-lg-6">
                <div className="card m-1">
                    <h6 className="card-header">
                        Salle {this.props.numero}
                    </h6>
                    <div className="card-body">
                        <div className="row">
                        <div className="col-6 mb-2">
                                    <h6>{this.state.currentProj.film.titre}</h6>
                                    <img width="180" height="240" src={'images/'+this.state.currentProj.film.photo} alt=""/>
                                </div>
                            <div className="col-auto">
                                <h6>Seances</h6>
                                <div className="list-group">
                                    {
                                        this.state.projections.map((proj, index) => 
                                            <li key={index} onClick={() => this.setCurrentProj(proj)}
                                                role="button"
                                                className={this.state.currentProj===proj?
                                                    "list-group-item bg-light":
                                                    "list-group-item"}>
                                                {
                                                    proj.seance.heureDebut 
                                                }
                                                &nbsp;<strong className="text-success">({proj.prix} DH)</strong>
                                            </li>    
                                        )
                                    }
                                </div>
                            </div>
                        </div>
                        <div className="row m-2">
                            {
                                this.state.showTickets===false?"":
                                    <TicketsBar projection={this.state.currentProj}/>
                            }
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
