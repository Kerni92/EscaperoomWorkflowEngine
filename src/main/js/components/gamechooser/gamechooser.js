const React = require('react');
import {makeStyles} from '@material-ui/core/styles';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';

const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 240,
    },
    selectEmpty: {
        marginTop: theme.spacing(2),
    },
}));


export class GameChooser extends React.Component {

    state = {
        games: [], selectedGame: ""
    };

    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(e) {
        this.setState({selectedGame: e.target.value})
        console.log("Changed to " + e.target.value);
        e.preventDefault();
    }


    async componentDidMount() {
        const response = await fetch("/api/ui/game/getAvailableWorkflows");
        const content = await response.json();
        this.setState({games: content});
    }


    render() {
        const {games} = this.state;
        const {selectedGame} = this.state;

        return (
            <FormControl className="useStyles.formControl">
                <Select id="gameselect" onChange={this.handleChange} value={selectedGame} name="Escaperoom">
                    {games.map(game =>
                        <MenuItem key={game.id} value={game.id}>{game.name}</MenuItem>
                    )}
                </Select>
            </FormControl>
        );
    }

}