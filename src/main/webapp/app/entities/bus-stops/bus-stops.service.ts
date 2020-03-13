import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBusStops } from 'app/shared/model/bus-stops.model';

type EntityResponseType = HttpResponse<IBusStops>;
type EntityArrayResponseType = HttpResponse<IBusStops[]>;

@Injectable({ providedIn: 'root' })
export class BusStopsService {
  public resourceUrl = SERVER_API_URL + 'api/bus-stops';

  constructor(protected http: HttpClient) {}

  create(busStops: IBusStops): Observable<EntityResponseType> {
    return this.http.post<IBusStops>(this.resourceUrl, busStops, { observe: 'response' });
  }

  update(busStops: IBusStops): Observable<EntityResponseType> {
    return this.http.put<IBusStops>(this.resourceUrl, busStops, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBusStops>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusStops[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
