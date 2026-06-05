import axios from 'axios';

const baseApiUrl = 'api/action-items';

export default class ActionItemService {
  getAll(pageable?: any): Promise<any> {
    return axios.get(baseApiUrl, { params: pageable }).then(res => res.data);
  }

  getOpen(): Promise<any> {
    return axios.get(`${baseApiUrl}/open`).then(res => res.data);
  }

  countOpen(): Promise<number> {
    return axios.get(`${baseApiUrl}/open/count`).then(res => res.data);
  }

  getOne(id: number): Promise<any> {
    return axios.get(`${baseApiUrl}/${id}`).then(res => res.data);
  }

  create(dto: any): Promise<any> {
    return axios.post(baseApiUrl, dto).then(res => res.data);
  }

  update(id: number, dto: any): Promise<any> {
    return axios.put(`${baseApiUrl}/${id}`, dto).then(res => res.data);
  }

  start(id: number): Promise<any> {
    return axios.post(`${baseApiUrl}/${id}/start`).then(res => res.data);
  }

  complete(id: number): Promise<any> {
    return axios.post(`${baseApiUrl}/${id}/complete`).then(res => res.data);
  }

  cancel(id: number): Promise<any> {
    return axios.post(`${baseApiUrl}/${id}/cancel`).then(res => res.data);
  }

  delete(id: number): Promise<void> {
    return axios.delete(`${baseApiUrl}/${id}`);
  }
}
