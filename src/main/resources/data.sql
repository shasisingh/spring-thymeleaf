CREATE TABLE IF NOT EXISTS HOSTEL
(
    id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(255),
    address        VARCHAR(255),
    capacity       INT NOT NULL DEFAULT 0,
    available      BOOLEAN,
    created_at     TIMESTAMP,
    contact_number VARCHAR(32),
    kvk            VARCHAR(32),
    building_type  VARCHAR(64),
    rating         DOUBLE PRECISION
);

-- Seed 50 hostels with initial capacity 0 (capacity will be managed by room operations)
INSERT INTO HOSTEL (name, address, capacity, available, created_at, contact_number, kvk, building_type, rating)
VALUES ('Backpackers Inn', '12 Hill St', 0, TRUE, CURRENT_TIMESTAMP, '0612345678', '12345678', 'Hostel', 4.7),
       ('City Hostel', '88 Main Ave', 0, TRUE, CURRENT_TIMESTAMP, '0623456789', '23456789', 'Hostel', 4.2),
       ('Quiet Corner', '5 Lake Rd', 0, FALSE, CURRENT_TIMESTAMP, '0634567890', '34567890', 'Hostel', 3.9),
       ('Sunset Lodge', '101 Sunset Blvd', 0, TRUE, CURRENT_TIMESTAMP, '0645678901', '45678901', 'Hostel', 4.8),
       ('Mountain View', '77 Peak St', 0, TRUE, CURRENT_TIMESTAMP, '0656789012', '56789012', 'Hostel', 4.5),
       ('Lakeside Hostel', '9 Lake Shore Dr', 0, TRUE, CURRENT_TIMESTAMP, '0667890123', '67890123', 'Hostel', 4.1),
       ('Urban Stay', '200 City Center Rd', 0, TRUE, CURRENT_TIMESTAMP, '0678901234', '78901234', 'Hostel', 4.6),
       ('Forest Retreat', '55 Pine Ave', 0, FALSE, CURRENT_TIMESTAMP, '0689012345', '89012345', 'Hostel', 3.7),
       ('Riverside Rest', '3 River Walk', 0, TRUE, CURRENT_TIMESTAMP, '0690123456', '90123456', 'Hostel', 4.3),
       ('Coastal Cabin', '444 Ocean Rd', 0, TRUE, CURRENT_TIMESTAMP, '0601234567', '01234567', 'Hostel', 4.0),
       ('Harbor House', '78 Dock St', 0, TRUE, CURRENT_TIMESTAMP, '0612345678', '12345678', 'Hostel', 4.4),
       ('Garden Hostel', '14 Rose Ln', 0, TRUE, CURRENT_TIMESTAMP, '0623456789', '23456789', 'Hostel', 4.1),
       ('City Lights', '310 Metro Ave', 0, TRUE, CURRENT_TIMESTAMP, '0634567890', '34567890', 'Hostel', 4.9),
       ('Country Inn', '5 Meadow Way', 0, FALSE, CURRENT_TIMESTAMP, '0645678901', '45678901', 'Hostel', 3.8),
       ('Skyline Stay', '900 Skyline Dr', 0, TRUE, CURRENT_TIMESTAMP, '0656789012', '56789012', 'Hostel', 4.6),
       ('Canyon Camp', '66 Canyon Rd', 0, TRUE, CURRENT_TIMESTAMP, '0667890123', '67890123', 'Hostel', 4.2),
       ('Bay Breeze', '21 Bay St', 0, TRUE, CURRENT_TIMESTAMP, '0678901234', '78901234', 'Hostel', 4.5),
       ('Hilltop Hostel', '8 Summit Ave', 0, TRUE, CURRENT_TIMESTAMP, '0689012345', '89012345', 'Hostel', 4.3),
       ('Downtown Dorms', '101 Center St', 0, TRUE, CURRENT_TIMESTAMP, '0690123456', '90123456', 'Hostel', 4.8),
       ('Maple House', '77 Maple St', 0, FALSE, CURRENT_TIMESTAMP, '0601234567', '01234567', 'Hostel', 3.6),
       ('Elm Stay', '9 Elm Rd', 0, TRUE, CURRENT_TIMESTAMP, '0612345678', '12345678', 'Hostel', 4.0),
       ('Cedar Cabin', '45 Cedar Ct', 0, TRUE, CURRENT_TIMESTAMP, '0623456789', '23456789', 'Hostel', 4.1),
       ('Birch Base', '88 Birch Blvd', 0, TRUE, CURRENT_TIMESTAMP, '0634567890', '34567890', 'Hostel', 4.4),
       ('Oak Outlook', '6 Oak Ln', 0, TRUE, CURRENT_TIMESTAMP, '0645678901', '45678901', 'Hostel', 4.7),
       ('Spruce Suites', '123 Spruce Ave', 0, TRUE, CURRENT_TIMESTAMP, '0656789012', '56789012', 'Hostel', 4.2),
       ('Palm Place', '3 Palm Dr', 0, TRUE, CURRENT_TIMESTAMP, '0667890123', '67890123', 'Hostel', 4.6),
       ('Shoreline Stay', '64 Shoreline Rd', 0, TRUE, CURRENT_TIMESTAMP, '0678901234', '78901234', 'Hostel', 4.3),
       ('Harbor Haven', '81 Harbor Rd', 0, TRUE, CURRENT_TIMESTAMP, '0689012345', '89012345', 'Hostel', 4.5),
       ('Pier Point', '72 Pier Ln', 0, TRUE, CURRENT_TIMESTAMP, '0690123456', '90123456', 'Hostel', 4.8),
       ('Marina Manor', '33 Marina Ave', 0, TRUE, CURRENT_TIMESTAMP, '0601234567', '01234567', 'Hostel', 4.9),
       ('Beacon Base', '16 Beacon St', 0, FALSE, CURRENT_TIMESTAMP, '0612345678', '12345678', 'Hostel', 3.7),
       ('Island Inn', '5 Island Way', 0, TRUE, CURRENT_TIMESTAMP, '0623456789', '23456789', 'Hostel', 4.4),
       ('Peninsula Place', '7 Peninsula Rd', 0, TRUE, CURRENT_TIMESTAMP, '0634567890', '34567890', 'Hostel', 4.6),
       ('Delta Dorm', '2 Delta St', 0, TRUE, CURRENT_TIMESTAMP, '0645678901', '45678901', 'Hostel', 4.5),
       ('Lagoon Lodge', '4 Lagoon Ln', 0, TRUE, CURRENT_TIMESTAMP, '0656789012', '56789012', 'Hostel', 4.3),
       ('Coral Cottage', '9 Coral Rd', 0, TRUE, CURRENT_TIMESTAMP, '0667890123', '67890123', 'Hostel', 4.1),
       ('Sandbar Stay', '19 Sandbar Ave', 0, TRUE, CURRENT_TIMESTAMP, '0678901234', '78901234', 'Hostel', 4.2),
       ('Cliffside Cabin', '27 Cliffside Rd', 0, TRUE, CURRENT_TIMESTAMP, '0689012345', '89012345', 'Hostel', 4.4),
       ('Meadow Mews', '6 Meadow Ln', 0, TRUE, CURRENT_TIMESTAMP, '0690123456', '90123456', 'Hostel', 4.0),
       ('Prairie Place', '12 Prairie Rd', 0, TRUE, CURRENT_TIMESTAMP, '0601234567', '01234567', 'Hostel', 4.1),
       ('Savanna Stay', '8 Savanna Ct', 0, TRUE, CURRENT_TIMESTAMP, '0612345678', '12345678', 'Hostel', 4.3),
       ('Valley View', '44 Valley Rd', 0, TRUE, CURRENT_TIMESTAMP, '0623456789', '23456789', 'Hostel', 4.5),
       ('Brookside Base', '3 Brookside Ln', 0, TRUE, CURRENT_TIMESTAMP, '0634567890', '34567890', 'Hostel', 4.2),
       ('Hillside Hostel', '11 Hillside Rd', 0, TRUE, CURRENT_TIMESTAMP, '0645678901', '45678901', 'Hostel', 4.4),
       ('Grove Guesthouse', '22 Grove Ave', 0, TRUE, CURRENT_TIMESTAMP, '0656789012', '56789012', 'Hostel', 4.6),
       ('Alpine Abode', '77 Alpine Rd', 0, TRUE, CURRENT_TIMESTAMP, '0667890123', '67890123', 'Hostel', 4.8),
       ('Tundra Terrace', '5 Tundra Way', 0, FALSE, CURRENT_TIMESTAMP, '0678901234', '78901234', 'Hostel', 3.9),
       ('Delta Dwell', '13 Delta Rd', 0, TRUE, CURRENT_TIMESTAMP, '0689012345', '89012345', 'Hostel', 4.7),
       ('Harbor Hub', '18 Harbor Ln', 0, TRUE, CURRENT_TIMESTAMP, '0690123456', '90123456', 'Hostel', 4.5),
       ('Bay Base', '7 Bay Ave', 0, TRUE, CURRENT_TIMESTAMP, '0601234567', '01234567', 'Hostel', 4.6);

CREATE TABLE IF NOT EXISTS REVIEW
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text    VARCHAR(500) NOT NULL,
    author  VARCHAR(100) NOT NULL,
    created TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO REVIEW (text, author, created)
VALUES ('I came for the WiFi, I stayed for the free breakfast. 10/10, would nap again!', 'Sleepy Sam',
        CURRENT_TIMESTAMP),
       ('Lost my socks, found new friends. Hostel magic!', 'Wanderlust Wendy', CURRENT_TIMESTAMP),
       ('Showers hotter than my love life. Highly recommend.', 'Steamy Steve', CURRENT_TIMESTAMP),
       ('Beds so comfy, I missed my train. Twice.', 'Snooze Cruise', CURRENT_TIMESTAMP),
       ('Staff laughed at my jokes. Or maybe at my hair. Either way, 5 stars!', 'Punny Paul', CURRENT_TIMESTAMP),
       ('I thought I was checking into a movie set. Turns out, it was just this awesome hostel!', 'Leonardo DiCaprio',
        CURRENT_TIMESTAMP),
       ('Even my entourage was impressed. And they are hard to please.', 'Dwayne Johnson', CURRENT_TIMESTAMP),
       ('I asked for a quiet room. They gave me a fan club instead!', 'Tom Holland', CURRENT_TIMESTAMP),
       ('The only thing missing was a red carpet. But the breakfast made up for it.', 'Scarlett Johansson',
        CURRENT_TIMESTAMP),
       ('I came for a night, stayed for a week. My agent is still looking for me.', 'Ryan Reynolds', CURRENT_TIMESTAMP),
       ('If hostels gave Oscars, this one would win Best Picture.', 'Meryl Streep', CURRENT_TIMESTAMP),
       ('I tried to go incognito, but the staff recognized me by my laugh.', 'Will Smith', CURRENT_TIMESTAMP),
       ('I’ve stayed in five-star hotels, but this place has five-star vibes.', 'Zendaya', CURRENT_TIMESTAMP),
       ('The only drama here is who gets the top bunk.', 'Chris Hemsworth', CURRENT_TIMESTAMP),
       ('I’d film my next blockbuster here if I could.', 'Gal Gadot', CURRENT_TIMESTAMP);

CREATE TABLE IF NOT EXISTS ROOM (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    room_number VARCHAR(32) NOT NULL,
    hostel_id BIGINT NOT NULL,
    allocated BOOLEAN NOT NULL DEFAULT FALSE,
    room_type VARCHAR(32) NOT NULL,
    CONSTRAINT fk_room_hostel FOREIGN KEY (hostel_id) REFERENCES HOSTEL(id)
);

CREATE TABLE IF NOT EXISTS ALLOCATION (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    dob DATE NOT NULL,
    email VARCHAR(255) NOT NULL,
    identity_doc VARCHAR(64) NOT NULL,
    payment_method VARCHAR(32) NOT NULL,
    hostel_room_number VARCHAR(32) NOT NULL,
    check_in TIMESTAMP NOT NULL,
    check_out TIMESTAMP NOT NULL,
    hostel_name VARCHAR(255),
    hostel_id BIGINT,
    room_id BIGINT,
    CONSTRAINT fk_allocation_hostel FOREIGN KEY (hostel_id) REFERENCES HOSTEL(id),
    CONSTRAINT fk_allocation_room FOREIGN KEY (room_id) REFERENCES ROOM(id)
);

-- Safe cleanup for legacy column in dev DBs
ALTER TABLE ALLOCATION DROP COLUMN IF EXISTS number_of_bed;
