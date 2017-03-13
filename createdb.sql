Create Table Movies(
mid INTEGER PRIMARY KEY,
title VARCHAR2(150),
imdbID VARCHAR2(150),
spanishTitle VARCHAR2(150),
imdbURL VARCHAR2(300),
year INTEGER,
rtID VARCHAR2(150),
rtAllCriticsRating NUMBER,
rtAllCriticsNumReviews INTEGER,
rtAllCriticsNumFresh INTEGER,
rtAllCriticsNumRotten INTEGER,
rtAllCriticsScore NUMBER,
rtTopCriticsRating NUMBER,
rtTopCriticsNumReviews INTEGER,
rtTopCriticsNumFresh INTEGER,
rtTopCriticsNumRotten INTEGER,
rtTopCriticsScore NUMBER,
rtAudienceRating NUMBER,
rtAudienceNumRatings INTEGER,
rtAudienceScore NUMBER,
rtURL VARCHAR2(300),
country VARCHAR(150)
);
Create Table Movie_Genres(
mid INTEGER,
genre VARCHAR2(50),
PRIMARY KEY(mid,genre),
FOREIGN KEY(mid) REFERENCES Movies
);

Create Table Movie_Directors(
mid INTEGER,
did VARCHAR2(50),
dname VARCHAR2(50),
PRIMARY KEY(mid,did),
FOREIGN KEY(mid) REFERENCES Movies
);
Create Table Movie_Actors(
mid INTEGER,
aid VARCHAR2(50),
aname VARCHAR2(50),
ranking INTEGER,
PRIMARY KEY(mid,aid),
FOREIGN KEY(mid) REFERENCES Movies
);

Create Table Tags(
tid INTEGER PRIMARY KEY,
value VARCHAR2(50)
);

Create Table Movie_Tags(
mid INTEGER,
tid INTEGER,
tagWeight INTEGER,
PRIMARY KEY(mid,tid),
FOREIGN KEY(mid) REFERENCES Movies,
FOREIGN KEY(tid) REFERENCES Tags
);
Create Table ImdbUser (
userid INTEGER
);

Create Table User_RatedMovie_Timedate(
userid INTEGER,
mid INTEGER,
rating NUMBER,
reviewday DATE,
PRIMARY KEY(userid,mid),
FOREIGN KEY(userid) REFERENCES ImdbUser,
FOREIGN KEY(mid) REFERENCES Movies
);

CREATE INDEX year_idx ON Movies(year);
CREATE INDEX rating1_idx ON Movies(rtAllCriticsRating);
CREATE INDEX rating2_idx ON Movies(rtTopCriticsRating);
CREATE INDEX rating3_idx ON Movies(rtAudienceRating);
CREATE INDEX numofreviews1_idx ON Movies(rtAllCriticsNumReviews);
CREATE INDEX numofreviews2_idx ON Movies(rtTopCriticsNumReviews);
CREATE INDEX numofreviews3_idx ON Movies(rtAudienceNumRatings);
CREATE INDEX country_idx ON Movies(country);

CREATE INDEX genre_idx ON Movie_Genres(genre);
CREATE INDEX genremid_idx ON Movie_Genres(mid);

CREATE INDEX dname_idx ON Movie_Directors(dname);
CREATE INDEX directormid_idx ON Movie_Directors(mid);

CREATE INDEX aname_idx ON Movie_Actors(aname);
CREATE INDEX actormid_idx ON Movie_Actors(mid);

CREATE INDEX tid_idx ON Tags(tid);

CREATE INDEX movietagstid_idx ON Movie_Tags(tid);
CREATE INDEX movietagsmid_idx ON Movie_Tags(mid);

CREATE INDEX rating_idx ON User_RatedMovie_Timedate(rating);
CREATE INDEX ratingmid_idx ON User_RatedMovie_Timedate(mid);
CREATE INDEX userid_idx ON User_RatedMovie_Timedate(userid);
CREATE INDEX reviewday_idx ON User_RatedMovie_Timedate(reviewday);

