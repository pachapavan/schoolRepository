import { IBusRoute } from 'app/shared/model/bus-route.model';

export interface IBusStops {
  id?: number;
  routeName?: string;
  busStops?: string;
  busRoute?: IBusRoute;
}

export class BusStops implements IBusStops {
  constructor(public id?: number, public routeName?: string, public busStops?: string, public busRoute?: IBusRoute) {}
}
