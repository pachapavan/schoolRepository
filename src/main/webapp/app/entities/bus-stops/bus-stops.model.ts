import { IBusRoute } from 'app/entities/bus-route/bus-route.model';

export interface IBusStops {
  id?: number;
  routeName?: string | null;
  busStops?: string | null;
  busRoute?: IBusRoute | null;
}

export class BusStops implements IBusStops {
  constructor(public id?: number, public routeName?: string | null, public busStops?: string | null, public busRoute?: IBusRoute | null) {}
}

export function getBusStopsIdentifier(busStops: IBusStops): number | undefined {
  return busStops.id;
}
