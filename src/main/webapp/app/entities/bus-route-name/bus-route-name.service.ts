import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBusRouteName } from 'app/shared/model/bus-route-name.model';

type EntityResponseType = HttpResponse<IBusRouteName>;
type EntityArrayResponseType = HttpResponse<IBusRouteName[]>;

@Injectable({ providedIn: 'root' })
export class BusRouteNameService {
  public resourceUrl = SERVER_API_URL + 'api/bus-route-names';

  constructor(protected http: HttpClient) {}

  create(busRouteName: IBusRouteName): Observable<EntityResponseType> {
    return this.http.post<IBusRouteName>(this.resourceUrl, busRouteName, { observe: 'response' });
  }

  update(busRouteName: IBusRouteName): Observable<EntityResponseType> {
    return this.http.put<IBusRouteName>(this.resourceUrl, busRouteName, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBusRouteName>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusRouteName[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
