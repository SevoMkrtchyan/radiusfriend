CREATE FUNCTION distance(lat1 DECIMAL, lon1 DECIMAL, lat2 DECIMAL, lon2 DECIMAL)
    RETURNS DECIMAL AS $$
DECLARE
    dLat          DECIMAL;
    dLon          DECIMAL;
    earthRadiusMetres INTEGER = 6371000;
    a             DECIMAL;
    c             DECIMAL;
BEGIN
    SELECT radians(lat2 - lat1) INTO dLat;
    SELECT radians(lon2 - lon1) INTO dLon;

    SELECT radians(lat1) INTO lat1;
    SELECT radians(lat2) INTO lat2;

    SELECT sin(dLat / 2) * sin(dLat / 2) +
           sin(dLon / 2) * sin(dLon / 2) * cos(lat1) * cos(lat2)
    INTO a;

    SELECT 2 * atan2(sqrt(a), sqrt(1 - a)) INTO c;
    RETURN earthRadiusMetres * c;

END;
$$ LANGUAGE plpgsql