import { IBusRoute } from 'app/shared/model/bus-route.model';

export interface IBusRouteName {
  id?: number;
  routeName?: string;
  busRoute?: IBusRoute;
}

export class BusRouteName implements IBusRouteName {
  constructor(public id?: number, public routeName?: string, public busRoute?: IBusRoute) {}
}
