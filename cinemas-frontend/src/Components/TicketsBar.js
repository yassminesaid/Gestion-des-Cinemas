import React, { Component } from 'react'
import axios from 'axios'

export default class TicketsBar extends Component {

    constructor(props) {
        super(props)
    
        this.state = {
            projection:null,
            tickets:[],
            ticketsToPay:[],
            paymentCode:null,
            username:''
        }
    }
    
    updateState() {
        this.setState({
            projection:this.props.projection
        }, () => this.getTickets());
    }

    componentDidMount() {
        this.updateState();
    }

    componentDidUpdate(prevProps, prevState) {
        if(prevProps === undefined || this.state.projection==null) {
            return false
        }

        if(this.state.projection.id !== this.props.projection.id) {
            this.updateState();
        }
    }

    getTickets() {
        let url = "http://localhost:8085/client/projections/"+this.state.projection.id+"/tickets";
        axios.get(url).then((resp) => {
            //console.log(resp.data);
            this.setState({
                tickets:resp.data
            })
        }).catch(err => {
            console.log(err);
        })
    }

    reserve(ticket) {
        if(ticket.reserve === true) return;
        else {
            if(this.state.ticketsToPay.includes(ticket)) {
                this.setState({
                    ticketsToPay:[...this.state.ticketsToPay].filter(item => item.id !== ticket.id)
                })
            }
            else {
                this.setState({
                    ticketsToPay:[...this.state.ticketsToPay, ticket]
                })
            }
        }
    }

    saveTickets = (event) => {
        event.preventDefault();
        this.state.ticketsToPay.map((ticket, index) => {
            ticket.reserve = true;
            ticket.nomClient = this.state.username;
            ticket.codePayement = this.state.paymentCode;
            ticket.projection = this.state.projection;
            //console.log(ticket)
            let url = "http://localhost:8085/client/tickets/"+ticket.id;
            axios.put(url, ticket).then((resp) => {
                //console.log(resp.data)
                let _tickets = [...this.state.tickets].filter(item => item.id !== ticket.id)
                this.setState({
                    tickets:[..._tickets, resp.data]
                })
            })
        })
    }

    annulerPayement() {
        this.setState({
            ticketsToPay:[]
        })
    }

    formChangeHandler = (event) => {
        let name = event.target.id;
        let val = event.target.value;
        this.setState({
            [name]:val
        })
    }

    render() {
        return (
            <div>
                <ul className="nav nav-pills">
                    {
                    this.state.tickets == null ? "" : 
                        this.state.tickets.map((ticket, index) => 
                            <li key={index}>
                                <button className={ticket.reserve===true?
                                    "btn btn-light m-1 disabled": this.state.ticketsToPay.includes(ticket)?
                                    "btn btn-warning m-1":"btn btn-success m-1"}
                                    onClick={() => this.reserve(ticket)}
                                >{ticket.place.numero}</button>
                            </li>
                        )
                    }
                </ul>
                {
                    this.state.ticketsToPay.length<=0?"":
                    <form onSubmit={this.saveTickets}>
                        <div className="form-group">
                            <label htmlFor="name">Nom et prénom : </label>
                            <input type="text" className="form-control" id="username" placeholder="Full name"
                                onChange={this.formChangeHandler}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="paymentCode">Code de payement : </label>
                            <input type="number" className="form-control" id="paymentCode" placeholder="Code de payement"
                                onChange={this.formChangeHandler}/>
                        </div>
                        <div className="form-group">
                            <button type="submit" className="btn btn-primary">Réserve</button>
                            <button type="reset" className="btn btn-primary ml-3"
                                onClick={() => this.annulerPayement()}>Annuller</button>
                        </div>
                    </form>
                }
            </div>
        )
    }
}
