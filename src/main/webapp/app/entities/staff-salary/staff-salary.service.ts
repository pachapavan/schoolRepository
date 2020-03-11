import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStaffSalary } from 'app/shared/model/staff-salary.model';

type EntityResponseType = HttpResponse<IStaffSalary>;
type EntityArrayResponseType = HttpResponse<IStaffSalary[]>;

@Injectable({ providedIn: 'root' })
export class StaffSalaryService {
  public resourceUrl = SERVER_API_URL + 'api/staff-salaries';

  constructor(protected http: HttpClient) {}

  create(staffSalary: IStaffSalary): Observable<EntityResponseType> {
    return this.http.post<IStaffSalary>(this.resourceUrl, staffSalary, { observe: 'response' });
  }

  update(staffSalary: IStaffSalary): Observable<EntityResponseType> {
    return this.http.put<IStaffSalary>(this.resourceUrl, staffSalary, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStaffSalary>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStaffSalary[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
