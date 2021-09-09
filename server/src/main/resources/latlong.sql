ALTER TABLE events
    ADD CONSTRAINT latlong check (latitude >= -90 AND latitude <= 90 AND
                                  longitude >= -180 AND longitude <= 180);