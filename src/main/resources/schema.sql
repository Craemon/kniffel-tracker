CREATE TABLE IF NOT EXISTS players (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS games (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS game_sheets (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    game_id INTEGER NOT NULL,
    player_id INTEGER NOT NULL,
    ones INTEGER,
    twos INTEGER,
    threes INTEGER,
    fours INTEGER,
    fives INTEGER,
    sixes INTEGER,
    three_of_a_kind INTEGER,
    four_of_a_kind INTEGER,
    full_house INTEGER,
    small_straight INTEGER,
    large_straight INTEGER,
    kniffel INTEGER,
    chance INTEGER,
    kniffel_bonus INTEGER,
    FOREIGN KEY (game_id) REFERENCES games(id),
    FOREIGN KEY (player_id) REFERENCES players(id),
    UNIQUE (game_id, player_id)
)