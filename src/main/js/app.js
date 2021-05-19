const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component {

    constructor(props) {
        super(props);
        // this.state = {employees: []};
    }

    componentDidMount() {
        (2)
        // client({method: 'GET', path: '/api/employees'}).done(response => {
        //     this.setState({employees: response.entity._embedded.employees});
        // });
    }

    render() {
        (3)
        return (
            <p>
                Hello react world
            </p>
        )
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('react')
)