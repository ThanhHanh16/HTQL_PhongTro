const db = require('../config/database');

const userModel = {
    findUserByUsername: (username, callback) => {
        db.get('SELECT * FROM users WHERE username = ?', [username], (err, row) => {
            callback(err, row);
        });
    }
};

module.exports = userModel;
