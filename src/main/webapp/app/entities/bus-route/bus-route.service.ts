import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBusRoute } from 'app/shared/model/bus-route.model';

type EntityResponseType = HttpResponse<IBusRoute>;
type EntityArrayResponseType = HttpResponse<IBusRoute[]>;

@Injectable({ providedIn: 'root' })
export class BusRouteService {
  public resourceUrl = SERVER_API_URL + 'api/bus-routes';

  constructor(protected http: HttpClient) {}

  create(busRoute: IBusRoute): Observable<EntityResponseType> {
    return this.http.post<IBusRoute>(this.resourceUrl, busRoute, { observe: 'response' });
  }

  update(busRoute: IBusRoute): Observable<EntityResponseType> {
    return this.http.put<IBusRoute>(this.resourceUrl, busRoute, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBusRoute>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusRoute[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
